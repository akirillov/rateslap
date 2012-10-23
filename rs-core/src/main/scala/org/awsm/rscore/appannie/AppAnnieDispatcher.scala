package org.awsm.rscore.appannie

import org.awsm.rscore.{NeedAuth, SourceCrawler}
import org.awsm.rscommons.{AuthObject, AuthResponse, StatsResponse, StatsRequest}
import xml.XML


/**
 * Created by: akirillov
 * Date: 10/21/12
 */

class AppAnnieDispatcher extends  SourceCrawler with NeedAuth{

  override  def getData(request: StatsRequest): StatsResponse = {
    //println(request)
    //println("Req JSON: "+request.generateJson())

    //TODO: val auth = authenticate(request.getAuth)

    //println(auth)

    //TODO: read file from remote store

    // val source = scala.io.Source.fromFile("resources/html/test_rank.html")

    val source = XML.loadFile("resources/html/test_rank.html")

    val parser = new AppAnnieHTMLParser()

    val result = parser.parse(source)

    if(result.hasError()) null else result.getData() //TODO: refactor to scala style



    //    StatsResponse("2012-10-21",  "My AWESOME App",  "Third world country",  "-1000")
  }

  override def authenticate(auth: AuthObject): AuthResponse = {
    // some auth actions go here
    AuthResponse(null, null, "No network connection!")
  }
}
