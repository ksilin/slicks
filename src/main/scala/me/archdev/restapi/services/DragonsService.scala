package me.archdev.restapi.services

import me.archdev.restapi.models.db.{DragonEntityTable, UserEntityTable}
import me.archdev.restapi.models.{DragonEntityUpdate, DragonEntity, UserEntityUpdate, UserEntity}
import org.mindrot.jbcrypt.BCrypt
import slick.backend.DatabasePublisher
import slick.driver
import slick.profile.SqlStreamingAction

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DragonsService extends DragonsService

trait DragonsService extends DragonEntityTable {

  import driver.api._

  def getDragons(): Future[Seq[DragonEntity]] = db.run(dragons.result)

  def getDragonsStream(): DatabasePublisher[DragonEntity] = db.stream(dragons.result)

  def getPlain(): Future[Vector[String]] = {
    val query: SqlStreamingAction[Vector[String], String, Effect] = sql"select name from Dragons where id < 3".as[String]
    db.run(query)
  }

  def getDragonById(id: Long): Future[Option[DragonEntity]] = db.run(dragons.filter(_.id === id).result.headOption)

  def createDragon(dragon: DragonEntity): Future[DragonEntity] = db.run(dragons returning dragons += dragon)

  def updateDragon(id: Long, DragonUpdate: DragonEntityUpdate): Future[Option[DragonEntity]] = getDragonById(id).flatMap {
    case Some(dragon) =>
      val updatedDragon = DragonUpdate.merge(dragon)
      db.run(dragons.filter(_.id === id).update(updatedDragon)).map(_ => Some(updatedDragon))
    case None => Future.successful(None)
  }

  def deleteDragon(id: Long): Future[Int] = db.run(dragons.filter(_.id === id).delete)

}