package org.awsm.rscommons

/**
 * Created by: akirillov
 * Date: 10/21/12
 */

case class AuthObject(username: String,  password: String)

object AuthObject{
  def create(username: String,  password: String): AuthObject = AuthObject(username,  password)
}
