package org.awsm.rscommons

/**
 * Created by: akirillov
 * Date: 10/21/12
 */

class AuthResponse(cookieKey: String,  cookieValue: String, error: String)

object AuthResponse {
  def apply(cookieKey: String,  cookieValue: String, error: String): AuthResponse = {
    new AuthResponse(cookieKey,  cookieValue, error)
  }
}