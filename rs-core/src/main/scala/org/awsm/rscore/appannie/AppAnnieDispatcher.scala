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

  override def getData(application: String, store: String, rankType: String, dates: List[String], countries: Set[String], auth: AuthObject): StatsResponse = {
    getData(dates, new AppAnnieCrawler(application, store, rankType, auth))
  }
  
  def getData(dates: List[String], crawler: AppAnnieCrawler): StatsResponse = {
    if (dates.size == 1) {

      val xml: String = crawler.crawl(dates.head) match {
        case None => throw NoResultsFoundException("No page found for "+dates.head) //todo: move to internal exception handling
        case Some(page) => page
      }

      parseAndBuildResponse(crawler.appName, crawler.store, crawler.rankType, dates.head, xml)

    } else {
      new StatsResponse("No data had been captured from AppAnnie service")
    }
  }

  def  parseAndBuildResponse(app: String,  store: String, rankType: String,  date: String, xml: String): StatsResponse = {
    val result = new AppAnnieXMLParser().parse(XML.loadString(xml))
    val ranks: scala.collection.mutable.Map[String, String] = new scala.collection.mutable.HashMap[String, String]()

    result.foreach(tuple => ranks.put(tuple._1, tuple._2))

    new StatsResponse(app, store, rankType, Map(date -> ranks.toMap))
  }
}
