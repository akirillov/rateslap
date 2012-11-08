package org.awsm.rscore.appannie

import org.awsm.rscore.{ParsingDispatcher}
import org.awsm.rscommons.{AuthObject, StatsResponse, StatsRequest}
import xml.XML


/**
 * Created by: akirillov
 * Date: 10/21/12
 */

class AppAnnieDispatcher extends ParsingDispatcher{

  override def getData(application: String, store: String, rankType: String, dates: List[String], country: Set[String], auth: AuthObject): StatsResponse = {
    val crawler: AppAnnieCrawler = new AppAnnieCrawler(application, store, rankType)
    val webClient = crawler.authenticate(auth)

    if (dates.size == 1) {
      val xml: String = crawler.crawl(webClient, dates.head) match {
        case None => "error" //todo: handle this case
        case Some(page) => page
      }

      val result = new AppAnnieXMLParser().parse(XML.loadString(xml))
    } else {


      /*
      if there are a list of dates:

        foreach new actor ! (webClient, date)

        */
    }
    
    


    //ELSE: list of dates -> parallel:


    /*

    lookup database:

    val result = lookupdDB(params) match {


     case none => authenticate and crawl
     case some => nothing to do





    */




    //todo: response generation
    new StatsResponse("Awful error occured!")
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
