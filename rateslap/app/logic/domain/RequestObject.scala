package logic.domain

import com.codahale.jerkson.Json._
import play.api.libs.json.{Json, JsValue}
import java.util.Date
import play.api.Logger

/**
 * Created by: akirillov
 * Date: 10/17/12
 */


object RequestBuilder{

  def buildRequestFromJson(json: JsValue): RequestObject = {

    //first validate auth object

    //second validate fields


    //store and date always single attributes

    val store = (json \ "store").asOpt[String] match {
      case Some(value) => value
      case None => ""//generate invalid parameter response
    }

    val date = (json \ "date").asOpt[String] match {
      case Some(value) => value //validate for far future
      case None => ""//generate invalid parameter response
    }

    //but title and region may be presented as lists of values
    val title =  (json \ "title").asOpt[String] match {
      case Some(value) => value //validate for far future
      case None => ""//generate invalid parameter response
    }

    val region =  (json \ "region").asOpt[String] match {
      case Some(value) => value //validate for far future
      case None => ""//generate invalid parameter response
    }

    Logger.info("\nCaptured values:\nstore: "+store+"\ndate: "+date+"\ntitle: "+title+"\nregion: "+region+"\n")
    new RequestObject(json)
  }

}


case class RequestObject (source: JsValue)