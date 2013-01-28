package org.awsm.rscore.appannie

import org.awsm.rscore.{ParsingDispatcher}
import org.awsm.rscommons.{AuthObject, StatsResponse, StatsRequest}
import xml.XML
import org.awsm.rscore.exception.NoResultsFoundException


/**
 * Created by: akirillov
 * Date: 10/21/12
 */

class AppAnnieDispatcher extends ParsingDispatcher{

  override def getData(application: String, store: String, rankType: String, dates: List[String], country: Set[String], auth: AuthObject): StatsResponse = {
    val crawler: AppAnnieCrawler = new AppAnnieCrawler(application, store, rankType)
    val webClient = crawler.authenticate(auth)

    if (dates.size == 1) { //todo: parse a list of dates after auth
      val xml: String = crawler.crawl(webClient, dates.head) match {
        case None => throw NoResultsFoundException("No page found for "+dates.head) //todo: move to internal exception handling
        case Some(page) => page
      }

      val result = new AppAnnieXMLParser().parse(XML.loadString(xml))
      val ranks: scala.collection.mutable.Map[String, String] = new scala.collection.mutable.HashMap[String, String]()

      result.foreach(tuple => ranks.put(tuple._1, tuple._2))

      //appName: String, store: String, rankings: Map[String, Map[String,  String]]
      new StatsResponse(application, store, Map(dates.head -> ranks.toMap))
    } else {
       new StatsResponse("No data had been captured from AppAnnie service")



    }
  }


  def buildParams(request: StatsRequest) = {
    val dates: List[String] = request.dates
    val appName: String = request.application.replaceAll(" ", "-").toLowerCase
    val store: String = if(request.store.replaceAll (" ","").toLowerCase.contains("appstore")) "ios" else request.store.toLowerCase
    val rankType: String = request.store match {
      case value: String => if(value.toLowerCase == "grossing") "grossing-ranks" else "ranks"
      case _ => "ranks"
    }

    (appName, store, rankType, dates)
  }
}
