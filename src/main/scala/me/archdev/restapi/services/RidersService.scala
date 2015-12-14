package me.archdev.restapi.services

import me.archdev.restapi.models.db.RiderEntityTable
import me.archdev.restapi.models.{RiderEntity, RiderEntityUpdate}
import shapeless.{::, HNil}
import slick.backend.DatabasePublisher
import slick.profile.SqlStreamingAction

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object RidersService extends RidersService

trait RidersService extends RiderEntityTable {

  import driver.api._

  type riderHList = Long :: String :: HNil

  def getRiders(): Future[Seq[riderHList]] = db.run(riders.result)

  def getRidersStream(): DatabasePublisher[riderHList] = db.stream(riders.result)

  def getPlain(): Future[Vector[String]] = {
    val query: SqlStreamingAction[Vector[String], String, slick.dbio.Effect] = sql"select name from Riders where id < 3".as[String]
    db.run(query)
  }

  def getRiderById(id: Long): Future[Option[riderHList]] = db.run(riders.filter(_.id === id).result.headOption)

  def createRider(rider: riderHList): Future[riderHList] = db.run(riders returning riders += rider)

  // TODO - how do I express an entity update with an HList?
  def updateRider(id: Long, RiderUpdate: RiderEntityUpdate): Future[Option[RiderEntity]] = getRiderById(id).flatMap {
    case Some(rider) =>
      val updatedRider = RiderUpdate.merge(rider)
      db.run(riders.filter(_.id === id).update(updatedRider)).map(_ => Some(updatedRider))
    case None => Future.successful(None)
  }

  def deleteRider(id: Long): Future[Int] = db.run(riders.filter(_.id === id).delete)

}