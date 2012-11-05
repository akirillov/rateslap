package org.awsm.rscore

import appannie.{AppAnnieCrawler, AppAnnieDispatcher}
import org.awsm.rscommons.{AuthObject, StatsRequest}


object TestRunner extends App {
  /*val dispatcher = new AppAnnieDispatcher

  val response = dispatcher.getData(StatsRequest("someMethod",  "MegaStore", "SouthAfrica", AuthObject("user", "secret")))

  println("Resp JSON: "+response.generateJson())*/


  //http://www.appannie.com/app/ios/cut-the-rope-hd/ranking/#view=ranks&date=2012-10-09

  val date = "2012-10-09"
  val appName = "Cut the rope HD"
  val store = "ios"
  val rankType = "rank"




  val crawler = new AppAnnieCrawler(date, appName, store, rankType)


  val xml = crawler.crawl(new AuthObject("anton.kirillov@zeptolab.com", "7ru57n01"))
  println(xml)
}
