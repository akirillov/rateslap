package logic

import play.api.libs.json.{Json, JsValue}


/**
 * Created by: akirillov
 * Date: 10/16/12
 */

class JsonError (code: Int, data: String) {
  val message = code match {
    case -32700 => "Parse error"
    case -32600 => "Invalid Request" //not valid request object todo: validate against spec?
    case -32601 => "Method not found"
    case -32602 => "Invalid params"
    case -32603 => "Internal error" //internal JSON-RPC error according to spec. We'll assume that any unpredictable Throwables go here
    case i: Int =>  if (i < -32000 && i > -32099) "Server error" else ""
  }

  def toJson():JsValue = {
    Json.toJson(
      Map(
        "code" -> Json.toJson(code),
        "message" -> Json.toJson(message),
        "data" -> Json.toJson(data)
      )
    )
  }
}
