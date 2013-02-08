package org.awsm.rsclient.json

import org.awsm.rscommons.StatsRequest

/**
 * Created by: akirillov
 * Date: 2/6/13
 */

case class RSRequest(
                      jsonrpc: String,
                      method: String,
                      params: StatsRequest,
                      id: String
                      )