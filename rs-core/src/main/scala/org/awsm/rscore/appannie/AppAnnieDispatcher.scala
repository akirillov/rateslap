package org.awsm.rscore.appannie

import org.awsm.rscore.{SourceCrawler}
import org.awsm.rscommons.{AuthObject, StatsResponse, StatsRequest}
import xml.XML


/**
 * Created by: akirillov
 * Date: 10/21/12
 */

class AppAnnieDispatcher extends SourceCrawler{

  def getData(request: StatsRequest): StatsResponse = {

    val params = buildParams(request)

    //todo: first goes database lookup, then - crawling for every date

    val crawler: AppAnnieCrawler = new AppAnnieCrawler(params._1, params._2, params._3)
    val webClient = crawler.authenticate(request.auth)




    val xml: String = crawler.crawl(webClient, params._4.head) match {
      case None => "error"
      case Some(page) => page
    }

    val source = XML.loadString(xml)
    val parser = new AppAnnieXMLParser()

    val result = parser.parse(source)

    //todo: response generation
    new StatsResponse("Awful error occured!")
  }


  def buildParams(request: StatsRequest) = {
    val dates: List[String] = request.date
    val appName: String = request.application.replaceAll(" ", "-").toLowerCase
    val store: String = if(request.store.replaceAll (" ","").toLowerCase.contains("appstore")) "ios" else request.store.toLowerCase
    val rankType: String = request.store match {
      case value: String => if(value.toLowerCase == "grossing") "grossing-ranks" else "ranks"
      case _ => "ranks"
    }

    (appName, store, rankType, dates)
  }

  override def crawl(request: StatsRequest) = null
}
