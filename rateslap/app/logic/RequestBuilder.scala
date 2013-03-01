package logic.domain

import play.api.libs.json.Json._
import play.api.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._
import logic.exception.JsonParamsException
import org.awsm.rscommons.{StatsResponse, AuthObject, StatsRequest}
import java.text.{ParseException, SimpleDateFormat}
import java.util.{Calendar, Date}

/**
 * Class represents JSON request parsing logic and forming StatsRequest.
 * Throws exception in case of wrong or absent parameters set.
 */
object RequestBuilder{

  private val format = new SimpleDateFormat("yyyy-MM-dd")
  format.setLenient(false)

  def getIdFromRequest(json: JsValue): Option[String] = {
    try {
      Some(getParameterFromJson(json, "id"))
    } catch {
      case p: JsonParamsException => None
    }
  }

  def buildRequestFromJson(json: JsValue): StatsRequest = {
    //TODO: move JSON parsing to commons, it is already implemented there with Spray-Json
    val params = (json  \ "params" ).asOpt[JsValue] match {
      case Some(value) => value
      case None => throw JsonParamsException("No \"params\" field found in JSON request!")
    }

    val application = getParameterFromJson(params, "application")
    val store = getParameterFromJson(params, "store")
    val rankType = getParameterFromJson(params, "rankType")

    val dates = getListFromJson(params, "dates")
    validateDates(dates)

    val countries = getSetFromJson(params, "countries")

    val authObject = getAuthObjectFromJson(params)

    new StatsRequest(application, store, rankType, dates, countries, authObject)
  }


  def getParameterFromJson(json: JsValue, fieldName: String): String = {
    (json \ fieldName).asOpt[String] match {
      case Some(value) => value
      case None => throw JsonParamsException("Parameter \""+fieldName+"\"` not specified in request!")
    }
  }

  def getListFromJson(json: JsValue, fieldName: String): List[String] = {
    (json \ fieldName).asOpt[List[String]] match {
      case Some(value) => value
      case None => throw JsonParamsException("Parameters \""+fieldName+"\"` not specified in request!")
    }
  }

  def getSetFromJson(json: JsValue, fieldName: String): Set[String] = {
    (json \ fieldName).asOpt[Set[String]] match {
      case Some(value) => value
      case None => throw JsonParamsException("Parameters \""+fieldName+"\"` not specified in request!")
    }
  }

  def getAuthObjectFromJson(json: JsValue): AuthObject = {

    val auth: JsValue = (json \ "authObject").asOpt[JsValue] match {
      case Some(value) => value
      case None => throw JsonParamsException("Auth Object not specified in request!")
    }

    new AuthObject(getParameterFromJson(auth, "username"),getParameterFromJson(auth, "password"))
  }


  def buildJsonResponse(request: StatsResponse, id: String): JsValue = {
    JsObject(
      Seq(
        "jsonrpc" -> JsString("2.0"),
        "result" -> JsObject(
          Seq(
            "application" -> JsString(request.application),
            "store" -> JsString(request.store),
            "rankType" -> JsString(request.rankType),

            if(request.rankings != null) { "rankings" -> Json.toJson (request.rankings)} else {"rankings" -> JsNull},

            "error" -> JsString(request.error)
          )
        ),
        "id" -> JsString(id)
      )
    )
  }

  def validateDates(dates: List[String]) {
    dates.foreach(d =>{
      if(d.length()!=10) throw JsonParamsException("Invalid date format provided in request! Format: yyyy-MM-dd")
        try{
          val date: Date = format.parse(d)
          if(!date.before(new Date())) throw JsonParamsException("The date from far future has been provided in request!")
        } catch {
          case e: ParseException => throw JsonParamsException("Invalid date format provided in request! Format: yyyy-MM-dd")
        }
    }
    )
  }
}