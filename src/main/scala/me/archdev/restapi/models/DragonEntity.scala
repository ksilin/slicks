package me.archdev.restapi.models

case class DragonEntity(id: Option[Long] = None, name: String, species: String, speed: Int, firepower: Int) {
  require(!name.isEmpty, "name.empty")
  require(!species.isEmpty, "species.empty")
}

case class DragonEntityUpdate(name: Option[String] = None, species: Option[String] = None, speed: Option[Int] = None,
                              firepower: Option[Int] = None) {
  def merge(dragon: DragonEntity): DragonEntity = {
    DragonEntity(dragon.id, name.getOrElse(dragon.name), species.getOrElse(dragon.species),
      speed.getOrElse(dragon.speed), firepower.getOrElse(dragon.firepower))
  }
}