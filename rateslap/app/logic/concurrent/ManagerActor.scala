package logic.concurrent

import play.Logger
import models.Rank
import org.awsm.rscommons.{StatsResponse, StatsRequest}
import actors.Actor._
import actors.{OutputChannel, Actor}
import org.awsm.rscore.appannie.{AppAnnieCrawler}
import logic.db.{SingleDateRequestWithData, SingleDateRequest}
import org.awsm.rscore.exception.NoResultsFoundException

/**
 * Created by: akirillov
 * Date: 1/24/13
 */

class ManagerActor extends Actor {

  def act() {
    var jobFinished = false
    var master: OutputChannel[Any] = null

    val resultSet = collection.mutable.Map[String, Map[String, String]]()
    var request: StatsRequest = null

    loopWhile(!jobFinished){
      react{

        case statsRequest: StatsRequest => {
          request = statsRequest
          master = sender
          Logger.info("INSIDE MANAGER ACTOR")
          lazy val crawler = new AppAnnieCrawler(statsRequest.application, statsRequest.store, statsRequest.rankType, statsRequest.auth)

          statsRequest.dates.foreach(date => {

            val singleDateRequest = SingleDateRequest(statsRequest.application,statsRequest.store, statsRequest.rankType, date)

            //here we are looking for entry in DB. If there is no entry for this date -> request from AppAnnie for this date
            val rank = Rank.find(singleDateRequest)

            Logger.info("DB result: "+ rank )

            if (rank.size == 0){
              //todo: remove duplicate code from here (hide inside crawler and throw exception)
              val xml: String = crawler.crawl(date) match {
                case None => throw NoResultsFoundException("No page found for "+date)
                case Some(page) => page
              }

              new ParserActor().start() ! SingleDateRequestWithData(statsRequest.application, statsRequest.store, statsRequest.rankType, date, xml)
            } else {
              //TODO: IMPLEMENT StatsResponse(Rank) :: NOT IN COMMONS

            }
          }
          )
        }

        case response: StatsResponse => {
          //here goes check for any error in the response and immediate exit if error not null
          if(response.error != null) {
            master.send(new StatsResponse("Error during capturing data: " + response.error) , self)
            exit()
          }

          resultSet put (response.rankings.head._1, response.rankings.head._2 filterKeys request.countries)

          new DBActor().start() ! response

          //todo: RIGHT HERE: capture parser actors results and put to DB in parallel

          if(resultSet.size == request.dates.size){
            master.send(new StatsResponse(request.application, request.store, request.rankType, resultSet.toMap, null) , self)
            jobFinished = true
          }
        }

        //todo: debug all actors stuff and construct a response
      }
    }
  }

  private def buildStatsResponse(rank: Rank): StatsResponse = {
    //TODO: IMPLEMENT
    new StatsResponse("Fully Monadic And Awesome!")
  }
}
