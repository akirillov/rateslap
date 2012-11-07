package org.awsm.rscommons

import java.util.Date
import java.text.SimpleDateFormat

/**
 * Created by: akirillov
 * Date: 11/7/12
 */

trait Response {

  def getRank(country: String, date: String = null): String

  def getRank(country: String,  date: Date): String = {
    if (date!=null) getRank (country, new SimpleDateFormat("yyyy-mm-dd").toString())
    else getRank(country)
  }

}
