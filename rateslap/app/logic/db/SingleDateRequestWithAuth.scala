package logic.db

import org.awsm.rscommons.AuthObject

/**
 * Created by: akirillov
 * Date: 11/29/12
 */

case class SingleDateRequestWithAuth(application: String, store: String, rankType: String, date: String, country: String,  auth: AuthObject)
//todo: create batch request
//todo: introduce store (maybe in next release)

object SingleDateRequestWithAuth {
  def apply(singleDateRequest: SingleDateRequest, auth: AuthObject): SingleDateRequestWithAuth = 
    SingleDateRequestWithAuth(singleDateRequest.game, "ios", singleDateRequest.rankType, singleDateRequest.date, singleDateRequest.country, auth)
}