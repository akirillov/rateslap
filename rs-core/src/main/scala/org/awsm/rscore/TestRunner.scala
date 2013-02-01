package org.awsm.rscore

import appannie.{AppAnnieXMLParser, AppAnnieCrawler, AppAnnieDispatcher}
import org.awsm.rscommons.{AuthObject, StatsRequest}
import xml.XML

/**
 * Created by: akirillov
 * Date: 1/31/13
 */

object TestRunner extends App {
  /*val dispatcher = new AppAnnieDispatcher

  val response = dispatcher.getData(StatsRequest("someMethod",  "MegaStore", "SouthAfrica", AuthObject("user", "secret")))

  println("Resp JSON: "+response.generateJson())*/


  //http://www.appannie.com/app/ios/cut-the-rope-hd/ranking/#view=ranks&date=2012-10-09

  val date = "2012-01-01"
  val appName = "Cut the rope"
  val store = "ios"
  val rankType = "ranks"
  //http://www.appannie.com/app/ios/cut-the-rope/ranking/#view=ranks&date=2012-10-09



  val crawler = new AppAnnieCrawler(appName, store, rankType, new AuthObject("user", "secret"))

 /* val dispatcher = new AppAnnieDispatcher()

  dispatcher.getData(date::List(), crawler)

  Thread.sleep(1000)*/

  val result = new AppAnnieDispatcher().getData(date::List(), crawler)
  /*val xml = crawler.crawl(date) match {
    case None => "error"
    case Some(page) => page
  }
*/
  //println(xml)

 /* val source = XML.loadString(xml)
  val parser = new AppAnnieXMLParser()

  val result = parser.parse(source)*/

  /* val xml = crawler.getData()


val source = XML.loadString(xml)
val parser = new AppAnnieXMLParser()

val result = parser.parse(source)*/
  println(result.rankings)

}
