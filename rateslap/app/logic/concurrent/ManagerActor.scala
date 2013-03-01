package logic.concurrent

import play.Logger
import models.Rank
import org.awsm.rscommons.{StatsResponse, StatsRequest}
import actors.Actor._
import actors.{OutputChannel, Actor}
import org.awsm.rscore.appannie.{AppAnnieCrawler}
import logic.db.{SingleDateRequestWithData}
import org.awsm.rscore.exception.NoResultsFoundException
import collection.mutable.SynchronizedMap

/**
 * Created by: akirillov
 * Date: 1/24/13
 */

class ManagerActor extends Actor {
  val resultSet = collection.mutable.Map[String, Map[String, String]]()

  def act() {
    var jobFinished = false
    var master: OutputChannel[Any] = null
    var request: StatsRequest = null

    def buildStatsResponse(ranks: Seq[Rank]): (String, Map[String, String]) = {
      var date = ""
      val result = new collection.mutable.HashMap[String, String] with SynchronizedMap[String, String]

      for(rank <- ranks) {
        if(date.equals("")){
          date = rank.date
        }
        result.put(rank.country, rank.rank.toString() )
      }

      (date, result.toMap)
    }

    def updateState() {
      if(resultSet.size == request.dates.size){
        master.send(new StatsResponse(request.application, request.store, request.rankType, resultSet.toMap, null) , self)
        jobFinished = true
      }
    }

    lazy val crawler = new AppAnnieCrawler(request.application, request.store, request.rankType, request.authObject)

    loopWhile(!jobFinished){
      react{

        case statsRequest: StatsRequest => {
          
          if(request == null){
            request = statsRequest
          }
          if(master == null){
            master = sender
          }

          statsRequest.dates.foreach(date => {
            //here we are looking for entry in DB. If there is no entry for this date -> request from AppAnnie for this date
            Logger.info("Trying to get data for "+date+ " from DB")
            val ranks = Rank.find(request.application, request.rankType, date, request.countries)

            if (ranks.size == 0){
              Logger.info("Data for "+date+ " not found in DB")
              new ParserActor(crawler).start ! SingleDateRequestWithData(date)
              Logger.debug("Request sent to parser")

            } else {
              Logger.info("Data for "+date+ " found in DB!")
              val dbResult = buildStatsResponse(ranks)

              Logger.info("dbResult._1 = "+dbResult._1+"  dbResult._2 = "+dbResult._2)
              resultSet put (dbResult._1, dbResult._2)

              updateState()
            }
          }
          )
        }

        case response: StatsResponse => {
          if(response.error != null) {
            master.send(new StatsResponse("Error during capturing data: " + response.error) , self)
            exit()
          }
          resultSet put (response.rankings.head._1, response.rankings.head._2.filterKeys(request.countries))

          new DBActor().start() ! response

          updateState()
        }
      }
    }
  }
}
