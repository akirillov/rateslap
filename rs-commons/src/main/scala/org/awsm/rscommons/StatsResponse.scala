package org.awsm.rscommons

import exception.{NoDataFoundException, RankNotFoundException, InvalidArgumentListException}
import java.util.Date
import java.text.SimpleDateFormat

/**
 * Response container class.
 * Contains part of provided by request information to verify similarity: application name and store.
 * Data presented in hierarchical manner and looks like this:
 *
 * date
 *     |_ country -> rank
 *     |_ country -> rank
 *     |_ ....
 *     |_ country -> rank
 * date
 *     |_ country -> rank
 *     ....
 *
 * Such design of the container is not straightforward, but provides unified interface
 * to access aggregated data in case of different request parameters combination (dates range or countries list or both)
 *
 * Created by: akirillov
 * Date: 10/17/12
 */

class StatsResponse(val appName: String,  val store: String, val rankings: Map[String, Map[String, String]], val error: String) extends  Response with JsonWrapper{
  def this(appName: String, store: String, rankings: Map[String, Map[String,  String]]) {
    this(appName, store, rankings, null)
  }

  def this(error: String){
    this(null,  null, null, error)
  }

  override def getRank(country: String, date: String): String =  {
    if (error != null) throw new NoDataFoundException("This response contains no data. Check error message for details")
    else date match {
      case null => if(rankings.size == 1) rankings.values.head.getOrElse(country, null)
                   else throw new InvalidArgumentListException("No specific date has been provided for list of ranks!")
      case d => rankings.getOrElse(d , throw new RankNotFoundException("Specified date is not presented in ranks list")).getOrElse(country, null)
    }
  }
}


