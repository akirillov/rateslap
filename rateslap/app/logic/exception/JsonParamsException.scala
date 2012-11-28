package logic.exception

/**
 * Created by: akirillov
 * Date: 11/28/12
 */

case class JsonParamsException(errorMsg:String)  extends Exception(errorMsg)

