package me.archdev.restapi.models.db

import me.archdev.restapi.utils.DatabaseConfig
import shapeless.{ HList, ::, HNil }
import slickless._

trait RiderEntityTable extends DatabaseConfig {

  import driver.api._

  class Riders(tag: Tag) extends Table[Long :: String :: HNil](tag, "riders") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def * = id :: name :: HNil
  }

  lazy val riders = TableQuery[Riders]
}
