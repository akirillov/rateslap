package logic.concurrent

import actors.Actor
import org.awsm.rscommons.{StatsResponse}
import org.awsm.rscommons.exception.RankNotFoundException
import org.awsm.rscore.exception.{ParsingException, NoResultsFoundException}
import org.awsm.rscore.appannie.{AppAnnieCrawler, AppAnnieDispatcher}
import logic.db.SingleDateRequestWithData
import play.api.Logger

/**
 * Created by: akirillov
 * Date: 11/29/12
 */

class ParserActor(val crawler: AppAnnieCrawler) extends Actor {

  def act() {
    react {
        case request: SingleDateRequestWithData => {
          Logger.debug("Request caught!")
          try{
            Logger.info("Trying to crawl data for "+request.date+" from source")
            val response = new AppAnnieDispatcher().getData(request.date::List(), crawler)
            Logger.info("Data captured for "+request.date)
            sender ! response
          } catch {
            case ex: ParsingException => sender ! new StatsResponse(ex.message)
            case pex: NoResultsFoundException => sender ! new StatsResponse("No results have been found. Exception message: "+pex.message)
            case pex: RankNotFoundException => sender ! new StatsResponse("No Rank has been found. Exception message: "+pex.message)
            case e: Exception => sender ! new StatsResponse("Exception occured: "+e.getMessage)
          }
        }
    }
  }
}
