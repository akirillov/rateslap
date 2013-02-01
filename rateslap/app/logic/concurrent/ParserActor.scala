package logic.concurrent

import actors.Actor
import play.Logger
import org.awsm.rscommons.{StatsResponse, AuthObject, StatsRequest}
import org.awsm.rscommons.exception.RankNotFoundException
import org.awsm.rscore.exception.{ParsingException, NoResultsFoundException}
import xml.XML
import org.awsm.rscore.appannie.{AppAnnieXMLParser, AppAnnieCrawler, AppAnnieDispatcher}
import logic.db.SingleDateRequestWithData._
import logic.db.SingleDateRequestWithData

/**
 * Created by: akirillov
 * Date: 11/29/12
 */

class ParserActor extends Actor {
  def act() {
    var response :StatsResponse = null
    loopWhile(response == null) {
      react {
        case request: SingleDateRequestWithData =>

          Logger.info("INSIDE PARSER ACTOR")

          try{

//            Logger.info("DEBUG:\ndates = "+request.date)

            response = new AppAnnieDispatcher().parseAndBuildResponse(request.application, request.store, request.rankType, request.date, request.xml)
//            Logger.info("DEBUG2:\nresult = "+response.rankings)
            //todo: build all the ranks according to the countries map

            //todo: put to DB and return results

            sender ! response

            //exit()

          } catch {
            case ex: ParsingException => sender ! new StatsResponse(ex.message)
            case pex: NoResultsFoundException => sender ! new StatsResponse("No results have been found. Exception message: "+pex.message)
            case pex: RankNotFoundException => sender ! new StatsResponse("No Rank has been found. Exception message: "+pex.message)
            case e: Exception => sender ! new StatsResponse("Exception occured: "+e.getMessage)
            /*case t: Throwable => {
              t.printStackTrace()
              sender ! new StatsResponse("Unknown Exception caught!")
            }*/
          }
      }
    }
  }
}
