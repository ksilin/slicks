package me.archdev.restapi.models


// TODO - dont we have the HList for that?
case class RiderEntity(id: Option[Long] = None, name: String) {
  require(!name.isEmpty, "name.empty")
}

case class RiderEntityUpdate(name: Option[String] = None) {
  def merge(dragon: RiderEntity): RiderEntity = {
    RiderEntity(dragon.id, name.getOrElse(dragon.name))
  }
}