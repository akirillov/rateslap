package org.awsm.rsclient.json

import spray.json.JsValue

/**
 * Created by: akirillov
 * Date: 2/7/13
 */

class RSError (val code: Int, val data: String) {
  val message = code match {
    case -32700 => "Parse error"
    case -32600 => "Invalid Request" //not valid request object todo: validate against spec?
    case -32601 => "Method not found"
    case -32602 => "Invalid params"
    case -32603 => "Internal error" //internal JSON-RPC error according to spec. We'll assume that any unpredictable Throwables go here
    case i: Int =>  if (i < -32000 && i > -32099) "Server error" else ""
  }
}
