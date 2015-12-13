package me.archdev.restapi.models.db

import me.archdev.restapi.models.DragonEntity
import me.archdev.restapi.utils.DatabaseConfig

trait DragonEntityTable extends DatabaseConfig {

  import driver.api._

  // TODO - still lots of unsafety - strings for table and column names - what will happen if these string would go wrong?
  class Dragons(tag: Tag) extends Table[DragonEntity](tag, "dragons") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def species = column[String]("species")
    def speed = column[Int]("speed")
    def firepower = column[Int]("firepower")

    def * = (id, name, species, speed, firepower) <> ((DragonEntity.apply _).tupled, DragonEntity.unapply)
  }

  protected val dragons = TableQuery[Dragons]

}
