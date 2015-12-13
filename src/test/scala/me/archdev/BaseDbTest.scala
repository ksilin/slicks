package me.archdev

import me.archdev.restapi.models.db._
import me.archdev.restapi.models.{DragonEntity, TokenEntity, UserEntity}
import me.archdev.restapi.utils.Migration
import org.scalatest._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await
import scala.concurrent.duration._

trait BaseDbTest extends WordSpec with Matchers with Migration
with DragonEntityTable with TokenEntityTable {
  protected val log: Logger = LoggerFactory.getLogger(this.getClass)

  val testUsers = Seq(
    UserEntity(Some(1), "Arhelmus", "test"),
    UserEntity(Some(2), "Arch", "test"),
    UserEntity(Some(3), "Hierarh", "test")
  )

  val testDragons = Seq(
    //  http://howtotrainyourdragon.wikia.com/wiki/Night_Fury
    //Attack   15
    //Speed 20
    //Armor 18
    //Firepower 14
    //Shot Limit  6
    //Venom    0
    //Jaw Strength  6
    // Stealth  18
    DragonEntity(Some(1), "Toothless", "Night Fury", 20, 14),

    //    http://howtotrainyourdragon.wikia.com/wiki/Gronckle
    //Attack   8
    //Speed 4
    //Armor 20
    //Firepower 14
    //Shot Limit  6
    //Venom    0
    //Jaw Strength  6
    // Stealth  5
    DragonEntity(Some(2), "Meatlug", "Gronckle", 4, 14),

    //    http://howtotrainyourdragon.wikia.com/wiki/Deadly_Nadder
    //Attack   10
    //Speed 8
    //Armor 18
    //Firepower 18
    //Shot Limit  6
    //Venom    16
    //Jaw Strength  5
    // Stealth  10
    DragonEntity(Some(3), "Stormfly", "Deadly Nadder", 8, 18),

    //    http://howtotrainyourdragon.wikia.com/wiki/Hideous_Zippleback
    //Attack   12
    //Speed 10
    //Armor 10
    //Firepower 14
    //Shot Limit  6
    //Venom    0
    //Jaw Strength  6
    // Stealth  22
    DragonEntity(Some(4), " Barf & Belch", "Hideous Zippleback", 10, 14)
  )

  val testTokens = Seq(
    TokenEntity(userId = Some(1)),
    TokenEntity(userId = Some(2)),
    TokenEntity(userId = Some(3))
  )

  // ++= comes from JdbcActionComponent in driver.api._
  import driver.api._
  reloadSchema()
  Await.result(db.run(users ++= testUsers), 10.seconds)
  Await.result(db.run(tokens ++= testTokens), 10.seconds)
  Await.result(db.run(dragons ++= testDragons), 10.seconds)
}
