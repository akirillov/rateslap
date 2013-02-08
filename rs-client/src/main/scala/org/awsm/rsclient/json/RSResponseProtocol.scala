package org.awsm.rsclient.json

import spray.json._
import org.awsm.rscommons.{StatsResponse}

/**
 * Created by: akirillov
 * Date: 2/6/13
 */

object RSResponseProtocol extends DefaultJsonProtocol {
  implicit object RSResponseJsonFormat extends RootJsonFormat[RSResponse] {
    def write(resp: RSResponse) = JsObject(
      "jsonrpc" -> JsString(resp.jsonrpc),
      if(resp.error != null)
      "error" -> JsObject(
        "code" ->JsNumber(resp.error.code),
        "data" -> JsString(resp.error.message)
      ) else "error" -> JsNull,
      "id" -> JsString(resp.id),

    if(resp.result != null)
      "result" -> JsObject(
        "application" -> JsString(resp.result.application),
        "store" -> JsString(resp.result.store),
        "rankType" -> JsString(resp.result.rankType),
        "rankings" -> resp.result.rankings.toJson
      ) else "result" -> JsNull
    )

    def read(value: JsValue) = {
      val res = value.asJsObject.getFields( "jsonrpc", "id", "error") match {
        case Seq(JsString(jsonrpc), JsString(id), error) =>
          error.asJsObject.getFields("code", "message") match {
            case Seq(JsNumber(code), JsString(message)) => RSResponse(jsonrpc = jsonrpc, id = id,  error =  new RSError(code.toInt, message), result = null)
            case _ => throw new DeserializationException("RSError expected!")
          }
        case _ => null
      }

      if (res!=null) res else {
        value.asJsObject.getFields( "jsonrpc", "id", "result") match {
          case Seq(JsString(jsonrpc), JsString(id), result) =>

            result.asJsObject.getFields("application", "store", "rankType", "rankings", "error") match {

              case Seq(JsString(application), JsString(store), JsString(rankType), rankings,  JsString(error)) =>
                RSResponse(jsonrpc = jsonrpc, id = id, result = new StatsResponse(application, store, rankType, rankings.convertTo[Map[String,  Map[String, String]]], error), error = null)

              case Seq(JsString(application), JsString(store), JsString(rankType), rankings,  JsNull) =>
                RSResponse(jsonrpc = jsonrpc, id = id, result = new StatsResponse(application, store, rankType, rankings.convertTo[Map[String,  Map[String, String]]]), error = null)

              case _ => throw new DeserializationException("StatsResponse expected!")
            }

          case _ => throw new DeserializationException("RSResponse expected!")
        }
      }
    }
  }
}