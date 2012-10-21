package org.awsm.rscore

import org.awsm.rscommons.{AuthObject, AuthResponse}


/**
 * Created by: akirillov
 * Date: 10/21/12
 */

trait NeedAuth {
    def authenticate(auth: AuthObject): AuthResponse
}
