package logic.concurrent

import play.Logger
import org.awsm.rscore.appannie.AppAnnieDispatcher
import logic.db.SingleDateRequest._
import models.Rank
import logic.db.SingleDateRequestWithAuth._
import org.awsm.rscommons.{StatsResponse, StatsRequest}
import logic.db.{SingleDateRequestWithAuth, SingleDateRequest}
import actors.Actor._
import actors.{OutputChannel, Actor}

/**
 * Created by: akirillov
 * Date: 1/24/13
 */

class ManagerActor extends Actor {

  def act() {
    var master: OutputChannel[Any] = null

    val resultSet = collection.mutable.Map[String, Map[String, String]]()
    var request: StatsRequest = null

    loop{
      react{

        case statsRequest: StatsRequest => {
          request = statsRequest
          master = sender
          Logger.info("INSIDE MANAGER ACTOR")

          //val ranks: scala.collection.mutable.Map[String, _ <: scala.collection.mutable.Map[String, String]] = new scala.collection.mutable.HashMap[String, scala.collection.mutable.HashMap[String, String]]()//country -> {date -> rank}

          statsRequest.dates.foreach(date => //todo: implement parallel DB lookup
            statsRequest.countries.foreach(country =>{      //here we are looking for entry in DB. If there is no entry for this date -> request from AppAnnie for this date

              val singleDateRequest = SingleDateRequest(statsRequest.application, statsRequest.rankType, date, country)

              val rank = Rank.find(singleDateRequest)

              Logger.info("DB result: "+ rank )

              //todo: collect all not found dates and countries and pass to parser

              if (rank.size == 0) new ParserActor().start() ! SingleDateRequestWithAuth(singleDateRequest, statsRequest.auth)
            }
            )
          )
        }

        case response: StatsResponse => {
          //here goes check for any error in the response and immediate exit if error not null
          if(response.error != null) {
            master.send(new StatsResponse("Error during capturing data: " + response.error) , self)
            exit()
          }

          resultSet put (response.rankings.head._1, response.rankings.head._2 filterKeys request.countries)

          //todo: RIGHT HERE: capture parser actors results and put to DB in parallel

          if(resultSet.size == request.dates.size){
            master.send(new StatsResponse(request.application, request.store, resultSet.toMap, null) , self)
            exit()
          }
          }

          //todo: debug all actors stuff and construct a response
      }
    }
  }
}
