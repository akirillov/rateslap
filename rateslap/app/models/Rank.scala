package models

import anorm.SqlParser._
import play.api.db.DB
import java.security.MessageDigest
import play.api.Play.current
import play.api.Logger
import anorm._

/**
 * Created by: akirillov
 * Date: 11/27/12
 */

case class Rank(id: Pk[Long] = NotAssigned, game: String, rankType: String, date: String,  country: String, rank: Int)

object Rank {

  val build = {
      get[Pk[Long]]("id") ~
      get[String]("game") ~
      get[String]("type") ~
      get[String]("date") ~
      get[String]("coutry") ~
      get[Int]("rank") map {
      case id~game~rankType~date~country~rank => Rank(id, game, rankType, date,  country, rank)
    }
  }

  // Create a row parser object
  val rowParser: RowParser[Pk[Long]~String~String~String~String~Int] = get[Pk[Long]]("id") ~ str("game") ~ str("type")  ~ str("date")  ~ str("country") ~int("rank")



  //todo: finish and test

  def find(game: String, rankType: String, date: String) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from ratings where
        game = {game}
        and type = {type}
        and date = {date}
      """
      ).on(
        'game -> game,
        'type -> rankType,
        'date -> date
//        'hash -> new String(MessageDigest.getInstance("MD5").digest((game+date).getBytes())) //we store data in single large table, so we'll use surrogate hash in index to speed up access
      ).list()
      //todo: http://www.playframework.org/documentation/2.0/ScalaAnorm
    }
  }

  def find(game: String, rankType: String, date: String, countries: Set[String]) = {
    DB.withConnection { implicit connection =>

    // Parse All Results from SQL Statement
      val rsp : ResultSetParser[List[Pk[Long]~String~String~String~String~Int]] = rowParser *

      SQL(
        """select * from ratings where
        game={game}
        and date={date}
        and type={type}
        and country in ('%s')
        """  format countries.mkString("','")
      ).on(
        'game -> game,
        'date -> date,
        'type -> rankType,
        'countries -> countries.mkString("','")
      ).as(rsp).map {
        case id~game~rankType~date~country~rank => Rank(id, game, rankType, date,  country, rank)
      }
    }
  }


  def insert(rank: Rank) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into ratings (game, type, date, country, rank, platform) values (
            {game}, {type}, {date}, {country}, {rank}, {platform}
          )
        """
      ).on(
        'game -> rank.game,
        'type -> rank.rankType,
        'date -> rank.date,
        'country -> rank.country,
        'rank -> rank.rank,
        'platform -> "ios"
        //'hash -> new String(MessageDigest.getInstance("MD5").digest((rank.game+rank.date).getBytes()))
      ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from ratings where id = {id}").on('id -> id).executeUpdate()
    }
  }

}
