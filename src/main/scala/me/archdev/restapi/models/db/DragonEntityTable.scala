package me.archdev.restapi.models.db

import me.archdev.restapi.models.DragonEntity
import me.archdev.restapi.utils.DatabaseConfig

trait DragonEntityTable extends DatabaseConfig {

  import driver.api._

  // TODO - still lots of type unsafety left - strings for table and column names
  class Dragons(tag: Tag) extends Table[DragonEntity](tag, "dragons") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def species = column[String]("species")

    def * = (id, name, species) <> ((DragonEntity.apply _).tupled, DragonEntity.unapply)
  }

  protected val dragons = TableQuery[Dragons]

}
