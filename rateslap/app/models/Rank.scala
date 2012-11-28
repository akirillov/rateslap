package models

import anorm.SqlParser._
import anorm.~._
import play.api.db.DB
import anorm._
import java.security.MessageDigest
import play.api.Play.current

/**
 * Created by: akirillov
 * Date: 11/27/12
 */

case class Rank(id: Pk[Long] = NotAssigned, game: String, platform: String, rankType: String, date: String,  country: String, rank: Int)

object Rank {

  val build = {
    get[Pk[Long]]("rank.id") ~
      get[String]("rank.game") ~
      get[String]("rank.platform") ~
      get[String]("rank.type") ~
      get[String]("rank.date") ~
      get[String]("rank.coutry") ~
      get[Int]("rank.rank") map {
      case id~game~platform~rankType~date~country~rank => Rank(id, game, platform, rankType, date,  country, rank)
    }
  }

  //todo: finish and test
  def find(game: String, platform: String, rankType: String, date: String, country: String) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from computer
        where hash = {hash}
        and game = {game}
        and platform = {platform}
        and type = {type}
        and date = {date}
        and country = {country}"

      """
      ).on(
        'game -> game,
        'platform -> platform,
        'type -> rankType,
        'date -> date,
        'country -> country,
        'hash -> new String(MessageDigest.getInstance("MD5").digest((game+platform).getBytes())) //we store data in single large table, so we'll use surrogate hash in index to speed up access
      ).list()
      //todo: http://www.playframework.org/documentation/2.0/ScalaAnorm
    }
  }

  def find(game: String, platform: String, rankType: String, dates: List[String], countries: List[String]) = {
    //todo: IN clause http://stackoverflow.com/questions/9528273/in-clause-in-anorm

    /*
    User.find("id in (%s)"
  .format(params.map("'%s'".format(_)).mkString(",") )
  .list()

    */

    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from computer
        where hash = {hash}
        and game = {game}
        and platform = {platform}
        and type = {type}
        and date IN ({dates})
        and country IN ({countries})"
      """
      ).on(
        'game -> game,
        'platform -> platform,
        'type -> rankType,
        'dates -> dates.reduceLeft((acc, s) => acc + "," + s),
        'countries -> countries.reduceLeft((acc, s) => acc + "," + s),
        'hash -> new String(MessageDigest.getInstance("MD5").digest((game+platform).getBytes())) //we store data in single large table, so we'll use surrogate hash in index to speed up access
      ).list()

    }
  }


  def insert(rank: Rank) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into ratings values (
            (select next value for ratings_seq),
            {game}, {platform}, {type}, {date}, {country}, {rank}, {hash}
          )
        """
      ).on(
        'game -> rank.game,
        'platform -> rank.platform,
        'type -> rank.rankType,
        'date -> rank.date,
        'country -> rank.country,
        'rank -> rank.rank,
        'hash -> new String(MessageDigest.getInstance("MD5").digest((rank.game+rank.platform).getBytes()))
      ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from ratings where id = {id}").on('id -> id).executeUpdate()
    }
  }

}
