package org.awsm.rsclient.json

import org.awsm.rscommons.StatsResponse

/**
 * Created by: akirillov
 * Date: 2/6/13
 */

/*
{"jsonrpc":"2.0",
"result":{"application":"Cut The Rope","store":"appstore",
"rankings":{"2012-05-01":{"United States":"9","Canada":"11"},
"2012-10-03":{"Canada":"17","United States":"27"},
"2012-03-02":{"Canada":"17","United States":"27"}},
"error":null},
"id":"1"}

*/

case class RSResponse (
                        jsonrpc: String,
                        error: RSError,
                        result: StatsResponse,
                        id: String
                        )