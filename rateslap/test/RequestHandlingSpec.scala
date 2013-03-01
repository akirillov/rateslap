import logic.domain.RequestBuilder
import logic.exception.JsonParamsException
import org.specs2.mutable._

import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._



/**
 * Created by: akirillov
 * Date: 2/18/13
 */

class RequestHandlingSpec extends Specification {
  val jsonStr = Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"store\":\"appstore\", \"dates\":[\"2012-01-01\",\"2012-01-02\"],\"rankType\":\"inapp\", \"countries\":[\"USA\",\"Canada\"], \"authObject\":{\"username\":\"user\", \"password\":\"secret\"}}, \"id\":\"731649\"}")

  
  //Building Request from JSON test
  "StatsRequest object for JSON String \"{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"store\":\"appstore\", \"dates\":[\"2012-01-01\",\"2012-01-02\"],\"rankType\":\"inapp\", \"countries\":[\"USA\",\"Canada\"], \"authObject\":{\"username\":\"user\", \"password\":\"secret\"}}, \"id\":\"1\"}" should {

    val statsRequest = RequestBuilder.buildRequestFromJson (jsonStr)

    "have correct \"application\" field" in {
      statsRequest.application must  equalTo ("Cut The Rope")
    }
    "have correct \"store\" field" in  {
      statsRequest.store must equalTo ("appstore")
    }
    "have correct \"rankType field" in {
      statsRequest.rankType must equalTo ("inapp")
    }
    "have correct \"countries\" size and values" in {
      statsRequest.countries must have size(2)
      statsRequest.countries must contain ("USA")
      statsRequest.countries must contain ("Canada")
    }
    "have correct \"dates\" size and values" in {
      statsRequest.dates must have size(2)
      statsRequest.dates must contain ("2012-01-01")
      statsRequest.dates must contain ("2012-01-02")
    }
    "AuthObject must not be null" in {
      statsRequest.authObject must not beNull
    }

    "AuthObject must have correct \"username\" and \"password\" fields" in {
      statsRequest.authObject.username must equalTo ("user")
      statsRequest.authObject.password must equalTo ("secret")
    }
  }

  //Building Request from JSON Exceptions test
  "StatsRequest object for JSON String missing one of the arguments" should {

    val jsonNoApp = Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"store\":\"appstore\", \"dates\":[\"2012-01-01\",\"2012-01-02\"],\"rankType\":\"inapp\", \"countries\":[\"USA\",\"Canada\"], \"authObject\":{\"username\":\"user\", \"password\":\"secret\"}}, \"id\":\"1\"}")
    val jsonNoStore = Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"dates\":[\"2012-01-01\",\"2012-01-02\"],\"rankType\":\"inapp\", \"countries\":[\"USA\",\"Canada\"], \"authObject\":{\"username\":\"user\", \"password\":\"secret\"}}, \"id\":\"1\"}")
    val jsonNoRankType = Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"store\":\"appstore\", \"dates\":[\"2012-01-01\",\"2012-01-02\"], \"countries\":[\"USA\",\"Canada\"], \"authObject\":{\"username\":\"user\", \"password\":\"secret\"}}, \"id\":\"1\"}")
    val jsonNoDates = Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"store\":\"appstore\", \"rankType\":\"inapp\", \"countries\":[\"USA\",\"Canada\"], \"authObject\":{\"username\":\"user\", \"password\":\"secret\"}}, \"id\":\"1\"}")
    val jsonNoCountries = Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"store\":\"appstore\", \"dates\":[\"2012-01-01\",\"2012-01-02\"],\"rankType\":\"inapp\", \"authObject\":{\"username\":\"user\", \"password\":\"secret\"}}, \"id\":\"1\"}")
    val noAuth = Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"store\":\"appstore\", \"dates\":[\"2012-01-01\",\"2012-01-02\"],\"rankType\":\"inapp\", \"countries\":[\"USA\",\"Canada\"]}, \"id\":\"1\"}")
    val jsonAuthNoUname = Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"store\":\"appstore\", \"dates\":[\"2012-01-01\",\"2012-01-02\"],\"rankType\":\"inapp\", \"countries\":[\"USA\",\"Canada\"], \"authObject\":{\"password\":\"secret\"}}, \"id\":\"1\"}")
    val jsonAuthNoPwd =  Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"store\":\"appstore\", \"dates\":[\"2012-01-01\",\"2012-01-02\"],\"rankType\":\"inapp\", \"countries\":[\"USA\",\"Canada\"], \"authObject\":{\"username\":\"user\"}}, \"id\":\"1\"}")

    "throw exception if \"application\" field is absent" in {
      RequestBuilder.buildRequestFromJson(jsonNoApp) must throwA[JsonParamsException]
    }
    "throw exception if \"store\" field is absent" in {
      RequestBuilder.buildRequestFromJson(jsonNoStore) must throwA[JsonParamsException]
    }

    "throw exception if \"rankType\" field is absent" in {
      RequestBuilder.buildRequestFromJson(jsonNoRankType) must throwA[JsonParamsException]
    }

    "throw exception if \"dates\" field is absent" in {
      RequestBuilder.buildRequestFromJson(jsonNoDates) must throwA[JsonParamsException]
    }

    "throw exception if \"countries\" field is absent" in {
      RequestBuilder.buildRequestFromJson(jsonNoCountries) must throwA[JsonParamsException]
    }

    "throw exception if \"authObject\" field is absent" in {
      RequestBuilder.buildRequestFromJson(noAuth) must throwA[JsonParamsException]
    }

    "throw exception if AuthObject' \"username\" field is absent" in {
      RequestBuilder.buildRequestFromJson(jsonAuthNoUname) must throwA[JsonParamsException]
    }

    "throw exception if AuthObject' \"password\" field is absent" in {
      RequestBuilder.buildRequestFromJson(jsonAuthNoPwd) must throwA[JsonParamsException]
    }
  }

  //getId() test
  "RequestBuilder.getId(json: JsValue)" should  {

    val noId = Json.parse("{\"jsonrpc\": \"2.0\", \"method\": \"getGamesStats\",\"params\": {\"application\":\"Cut The Rope\", \"store\":\"appstore\", \"dates\":[\"2012-01-01\",\"2012-01-02\"],\"rankType\":\"inapp\", \"countries\":[\"USA\",\"Canada\"], \"authObject\":{\"username\":\"user\", \"password\":\"secret\"}}}")

    "throw exception if \"id\" field is absent" in  {
      RequestBuilder.getIdFromRequest(noId) must throwA[JsonParamsException]
    }
    "return proper \"id\" value from request" in {
      RequestBuilder.getIdFromRequest(jsonStr) must equalTo ("731649")
    }
  }
  //validateDates test
  "RequestBuilder.validateDates(dates: List[String])" should  {

    val validDates = List("2012-01-01", "2012-01-02", "2012-01-03")
    val invalidDateFormat0 = List("20120101")
    val invalidDateFormat1 = List("2012-1-2")
    val invalidDateFormat2 = List("2012.01.02")
    val farFutureDate = List("2063-01-01")

    "validate List of correct dates" in {
      RequestBuilder.validateDates(validDates) must not (throwA[JsonParamsException])
    }
    "throw exception on List of dates in wrong format" in {
      RequestBuilder.validateDates(invalidDateFormat0) must throwA[JsonParamsException]
      RequestBuilder.validateDates(invalidDateFormat1) must throwA[JsonParamsException]
      RequestBuilder.validateDates(invalidDateFormat2) must throwA[JsonParamsException]
    }
    "throw exception in case of date representing far future" in {
      RequestBuilder.validateDates(farFutureDate) must throwA[JsonParamsException]
    }
    
  }
}
