package org.awsm.rscommons

import com.codahale.jerkson.Json._

/**
 * Created by: akirillov
 * Date: 10/21/12
 */

trait JsonWrapper {
  def generateJson(): String = generate(this)
}
