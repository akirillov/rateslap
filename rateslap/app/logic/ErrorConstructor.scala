package logic

import play.api.libs.json.{Json, JsValue}
import logic.ErrorConstructor.ErrorResponse


/**
 * Created by: akirillov
 * Date: 10/16/12
 */

object ErrorConstructor {

  def constructError(id: Option[String], code: Int, data: String): JsValue = {
    val response = new ErrorResponse(id match {
      case null => null
      case Some(id) => id
      case None => null
    }, new JsonError(code, data).toJson())
    response.toJson
  }

  class ErrorResponse(id: String,  error: JsValue){
    def toJson(): JsValue = {
      Json.toJson(
        Map(
          "jsonrpc" -> Json.toJson ("2.0"),
          "id" -> Json.toJson (id),
          "error" -> error
        )
      )
    }
  }
}
