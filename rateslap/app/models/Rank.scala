package models

import anorm.SqlParser._
import play.api.db.DB
import java.security.MessageDigest
import play.api.Play.current
import anorm._
import logic.db.SingleDateRequest

/**
 * Created by: akirillov
 * Date: 11/27/12
 */

case class Rank(id: Pk[Long] = NotAssigned, game: String, rankType: String, date: String,  country: String, rank: Int)

object Rank {

  val build = {
    get[Pk[Long]]("rank.id") ~
      get[String]("rank.game") ~
      get[String]("rank.type") ~
      get[String]("rank.date") ~
      get[String]("rank.coutry") ~
      get[Int]("rank.rank") map {
      case id~game~rankType~date~country~rank => Rank(id, game, rankType, date,  country, rank)
    }
  }

  //todo: finish and test

  /**
   * Overloaded find for SingleDateRequest wrapper
   * @param request - request object
   * @return list of corresponding entries
   */
  def find(request: SingleDateRequest): Seq[Row] = {
    find(game = request.application, rankType = request.rankType, date = request.date/*, country = request.country*/)
  }

  def find(game: String, rankType: String, date: String) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from ratings
        where hash = {hash}
        and game = {game}
        and type = {type}
        and date = {date}
      """
      ).on(
        'game -> game,
        'type -> rankType,
        'date -> date,
        'hash -> new String(MessageDigest.getInstance("MD5").digest((game+date).getBytes())) //we store data in single large table, so we'll use surrogate hash in index to speed up access
      ).list()
      //todo: http://www.playframework.org/documentation/2.0/ScalaAnorm
    }
  }

  def find(game: String, rankType: String, dates: List[String], countries: List[String]) = {
    //todo: IN clause http://stackoverflow.com/questions/9528273/in-clause-in-anorm

    /*
    User.find("id in (%s)"
  .format(params.map("'%s'".format(_)).mkString(",") )
  .list()

    */

    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from ratings
        where hash = {hash}
        and game = {game}
        and type = {type}
        and date IN ({dates})
        and country IN ({countries})
      """
      ).on(
        'game -> game,
        'type -> rankType,
        'dates -> dates.reduceLeft((acc, s) => acc + "," + s),
        'countries -> countries.reduceLeft((acc, s) => acc + "," + s)
        //'hash -> new String(MessageDigest.getInstance("MD5").digest((game+date).getBytes())) //we store data in single large table, so we'll use surrogate hash in index to speed up access
      ).list()

    }
  }


  def insert(rank: Rank) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into ratings (game, type, date, country, rank, platform, hash) values (
            {game}, {type}, {date}, {country}, {rank}, {platform}, {hash}
          )
        """
      ).on(
        'game -> rank.game,
        'type -> rank.rankType,
        'date -> rank.date,
        'country -> rank.country,
        'rank -> rank.rank,
        'platform -> "ios",
        'hash -> new String(MessageDigest.getInstance("MD5").digest((rank.game+rank.date).getBytes()))
      ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from ratings where id = {id}").on('id -> id).executeUpdate()
    }
  }

}
