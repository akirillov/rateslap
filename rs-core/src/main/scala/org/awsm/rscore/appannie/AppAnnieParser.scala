package org.awsm.rscore.appannie

import org.awsm.rscore.{NeedAuth, SourceParser}
import org.awsm.rscommons.{AuthObject, AuthResponse, StatsResponse, StatsRequest}


/**
 * Created by: akirillov
 * Date: 10/21/12
 */

class AppAnnieParser extends  SourceParser with NeedAuth{



  override  def getData(request: StatsRequest): StatsResponse = {
    println(request)
    println("Req JSON: "+request.generateJson())

   val auth = authenticate(request.getAuth)

    println(auth)

    //matching error cases and then go further or throw error

    StatsResponse("2012-10-21",  "My AWESOME App",  "Third world country",  "-1000")
  }

  override def authenticate(auth: AuthObject): AuthResponse = {
    // some auth actions go here
    AuthResponse(null, null, "No network connection!")
  }
}
