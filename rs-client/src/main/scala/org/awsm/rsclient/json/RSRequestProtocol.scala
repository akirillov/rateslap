package org.awsm.rsclient.json

import spray.json._
import DefaultJsonProtocol._
import org.awsm.rscommons.{StatsRequest, AuthObject}

/**
 * Created by: akirillov
 * Date: 2/6/13
 */

object RSRequestProtocol extends DefaultJsonProtocol {
  implicit object RSRequestJsonFormat extends RootJsonFormat[RSRequest] {
    def write(req: RSRequest) = JsObject(
      "jsonrpc" -> JsString(req.jsonrpc),
      "method" -> JsString(req.method),
      "id" -> JsString(req.id),

      "params" -> JsObject(
        "application" -> JsString(req.params.application),
        "store" -> JsString(req.params.store),
        "rankType" -> JsString(req.params.rankType),
        "dates" -> req.params.dates.toJson,
        "countries" -> req.params.countries.toJson,
        "authObject" -> JsObject(
          "username" -> JsString(req.params.authObject.username),
          "password" -> JsString(req.params.authObject.password)
        )
      )
    )

    def read(value: JsValue) = {
      value.asJsObject.getFields( "jsonrpc", "method", "id", "params") match {
        case Seq(JsString(jsonrpc), JsString(method), JsString(id), params) =>    {
          RSRequest(jsonrpc, method,

            params.asJsObject.getFields ("application", "store", "rankType", "dates", "countries", "authObject") match {
              case  Seq(JsString(application), JsString(store), JsString(rankType), JsArray(dates),  JsArray(countries), authObject) =>
                new StatsRequest(
                  application,
                  store,
                  rankType,
                  dates.toList.map(json => json.toString()),
                  countries.toList.map(json => json.toString()).toSet,
                    authObject.asJsObject.getFields("username", "password") match {
                      case Seq(JsString(username), JsString(password)) => AuthObject(username, password)
                      case _ => throw new DeserializationException("AuthObject expected!")
                    }
                )
              case _ => throw new DeserializationException("RSDataHandler expected!")
            }
            ,id)
        }
        case _ => throw new DeserializationException("RSRequest expected!")
      }
    }
    }
}