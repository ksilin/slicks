package me.archdev.restapi.utils


// TODO - why is there a Config &  DatabaseConfig?
trait DatabaseConfig {
  val driver = slick.driver.PostgresDriver

  import driver.api._

  def db = Database.forConfig("db")

  implicit val session: Session = db.createSession()
}
