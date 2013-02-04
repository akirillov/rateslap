package logic.concurrent

import models.Rank
import logic.db.{Stop}
import actors.Actor
import org.awsm.rscommons.{StatsResponse, StatsRequest}
import anorm.NotAssigned
import play.Logger

/**
 * Created by: akirillov
 * Date: 11/29/12
 */

class DBActor extends Actor {
  def act() {
    receive {
      case response: StatsResponse => {
        if(Option(response.error).isEmpty && !response.rankings.isEmpty){
          response.rankings foreach {
            case (key, value) => {
              value foreach {
                case (country, rank) => {
                  if (rank.matches("[+-]?\\d+")) {
                    Rank.insert(Rank(id = NotAssigned, game = response.application, rankType = response.rankType, date = key,  country, Integer.parseInt(rank)))
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
