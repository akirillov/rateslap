package logic.concurrent

import models.Rank
import org.awsm.rscommons.StatsRequest
import logic.db.{Stop, SingleDateRequest}
import actors.Actor

/**
 * Created by: akirillov
 * Date: 11/29/12
 */

class DBActor extends Actor {
  def act() {
    loop {
      react {
        case request: SingleDateRequest =>
          sender ! Rank.find(request.game, request.rankType, request.date, request.country)
      }
    }
  }
}
