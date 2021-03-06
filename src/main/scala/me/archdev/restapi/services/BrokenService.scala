package me.archdev.restapi.services

import me.archdev.restapi.models.db.UserEntityTable
import me.archdev.restapi.models.{UserEntity, UserEntityUpdate}
import slick.profile.SqlStreamingAction

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}

object BrokenService extends BrokenService

trait BrokenService extends UserEntityTable {

  import driver.api._

  def getFailedFuture(): Future[Seq[UserEntity]] = Future.failed(new Throwable("fail"))

  def getFailingFuture(): Future[Seq[UserEntity]] = Future{ blocking(Thread.sleep(100L)); throw new Throwable("fail") }

  def getPlain(): Future[Vector[String]] = {
    val query: SqlStreamingAction[Vector[String], String, Effect] = sql"select username from users where id < 3".as[String]
    db.run(query)
  }

  def getUserById(id: Long): Future[Option[UserEntity]] = db.run(users.filter(_.id === id).result.headOption)

  def getUserByLogin(login: String): Future[Option[UserEntity]] = db.run(users.filter(_.username === login).result.headOption)

  def createUser(user: UserEntity): Future[UserEntity] = db.run(users returning users += user.withHashedPassword())

  def updateUser(id: Long, userUpdate: UserEntityUpdate): Future[Option[UserEntity]] = getUserById(id).flatMap {
    case Some(user) =>
      val updatedUser = userUpdate.merge(user)
      db.run(users.filter(_.id === id).update(updatedUser)).map(_ => Some(updatedUser))
    case None => Future.successful(None)
  }

  def deleteUser(id: Long): Future[Int] = db.run(users.filter(_.id === id).delete)

}