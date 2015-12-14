package me.archdev

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import me.archdev.restapi.models.DragonEntity
import me.archdev.restapi.services.DragonsService
import org.scalatest.concurrent.ScalaFutures
import slick.backend.DatabasePublisher

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}


class DragonsServiceDbTest extends BaseDbTest with ScalaFutures {

  private implicit val system = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  "Dragons service" should {

    "retrieve dragons list" in {
      val dragons: Future[Seq[DragonEntity]] = DragonsService.getDragons()
      val dragonEntities: Seq[DragonEntity] = Await.result(dragons, 10.seconds)
      log.info(s"dragons: $dragonEntities")
    }

    "retrieve dragons stream" in {
      val dragons: DatabasePublisher[DragonEntity] = DragonsService.getDragonsStream()

      val count: Future[Int] = Source(dragons).runFold(0)((cnt, _) => cnt + 1)

      count map {c: Int => c should be(2) }
      val res = Await.result(count, 10.seconds)
      res should be(5)
    }

    "retrieve and test async" in {
      val dragonNamesFuture: Future[Seq[String]] = DragonsService.getPlain()
      dragonNamesFuture map { u => u should have size 3 }
    }

    "retrieve by plain sql list" in {
      val dragonNamesFuture: Future[Seq[String]] = DragonsService.getPlain()
      val dragonNames: Seq[String] = Await.result(dragonNamesFuture, 10.seconds)
      log.info(s"dragons: $dragonNames")
    }

    "retrieve by plain sql list 2" in {
      val dragonNamesFuture: Future[Seq[String]] = DragonsService.getPlain()

      dragonNamesFuture.onComplete {
        _ match {
          case Success(n) => println(s"complete. dragon names: $n")
          case Failure(ex) => println(s"complete. failed to get dragon names because of $ex")
        }
      }

      // TODO - test recovery with retry - recursion
//      dragonNamesFuture.recoverWith()

      Await.result(dragonNamesFuture, 10.seconds)
    }
  }

  "retrieve dragon by id" in {
    //      Get("/dragons/1") ~> dragonsRoute ~> check {
    //        responseAs[JsObject] should be(testDragons.head.toJson)
    //      }
  }

  "update dragon by id and retrieve it" in {
    //      val newDragonname = "UpdatedDragonname"
    //      val requestEntity = HttpEntity(MediaTypes.`application/json`, JsObject("dragonname" -> JsString(newDragonname)).toString())
    //      Post("/dragons/1", requestEntity) ~> dragonsRoute ~> check {
    //        responseAs[JsObject] should be(testDragons.head.copy(dragonname = newDragonname).toJson)
    //        whenReady(getDragonById(1)) { result =>
    //          result.get.dragonname should be(newDragonname)
    //        }
    //      }
  }

  "delete dragon" in {
    //      Delete("/dragons/3") ~> dragonsRoute ~> check {
    //        response.status should be(NoContent)
    //        whenReady(getDragonById(3)) { result =>
    //          result should be(None: Option[DragonEntity])
    //        }
    //      }
  }

  "retrieve currently logged dragon" in {
    //      Get("/dragons/me") ~> addHeader("Token", testTokens.find(_.dragonId.contains(2)).get.token) ~> dragonsRoute ~> check {
    //        responseAs[JsObject] should be(testDragons.find(_.id.contains(2)).get.toJson)
    //      }
  }

  "update currently logged dragon" in {
    //      val newDragonname = "MeUpdatedDragonname"
    //      val requestEntity = HttpEntity(MediaTypes.`application/json`, JsObject("dragonname" -> JsString(newDragonname)).toString())
    //      Post("/dragons/me", requestEntity) ~> addHeader("Token", testTokens.find(_.dragonId.contains(2)).get.token) ~> dragonsRoute ~> check {
    //        responseAs[JsObject] should be(testDragons.find(_.id.contains(2)).get.copy(dragonname = newDragonname).toJson)
    //        whenReady(getDragonById(2)) { result =>
    //          result.get.dragonname should be(newDragonname)
    //        }
    //      }
    //    }
  }

}
