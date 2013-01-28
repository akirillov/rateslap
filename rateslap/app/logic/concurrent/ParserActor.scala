package logic.concurrent

import logic.db.{SingleDateRequestWithAuth}
import actors.Actor
import org.awsm.rscore.appannie.AppAnnieDispatcher
import play.Logger
import org.awsm.rscommons.{StatsResponse, AuthObject, StatsRequest}
import org.awsm.rscommons.exception.RankNotFoundException
import org.awsm.rscore.exception.{ParsingException, NoResultsFoundException}

/**
 * Created by: akirillov
 * Date: 11/29/12
 */

class ParserActor extends Actor {
  def act() {
    loop {
      react {
        case request: SingleDateRequestWithAuth =>

          Logger.info("INSIDE PARSER ACTOR")

          try{
          val response = new AppAnnieDispatcher().getData(new StatsRequest(request.application, request.store, request.rankType, request.date::List(), request.country, request.auth)  )

          Logger.info(response.getRank(request.date, request.country))

          //todo: build all the ranks according to the countries map

          //todo: put to DB and return results

          sender ! response

          exit()

          } catch {
            case ex: ParsingException => sender ! new StatsResponse(ex.message)
            case pex: NoResultsFoundException => sender ! new StatsResponse("No results have been found. Exception message: "+pex.message)
            case pex: RankNotFoundException => sender ! new StatsResponse("No Rank has been found. Exception message: "+pex.message)
            case e: Exception => sender ! new StatsResponse("Exception occured: "+e.getMessage)
            case _ => sender ! new StatsResponse("Unknown Exception caught!")
          }
      }
    }
  }
}
