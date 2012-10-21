import logic.ErrorConstructor
import play.api._
import mvc.{Results, Result, RequestHeader}

/**
 * Created by: akirillov
 * Date: 10/17/12
 */

object Global extends GlobalSettings {

     override def onError(request : RequestHeader, ex : Throwable): Result = {
       Results.Ok(ErrorConstructor.constructError(null, -32603, "Internal error occured: "+ex.getMessage))
     }

   //As we providing full JSON-RPC 2.0 implementation we should catch and wrap bad requests
  //corresponding to specification
  override def onBadRequest(request : RequestHeader, error : String): Result = {
    Results.Ok(ErrorConstructor.constructError(null, -32700, "Invalid JSON was received by the server. An error occurred on the server while parsing the JSON text."))
  }
}
