package me.archdev

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import me.archdev.restapi.models.UserEntity
import me.archdev.restapi.services.UsersService
import org.scalatest.concurrent.ScalaFutures
import slick.backend.DatabasePublisher

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global


class UsersServiceDbTest extends BaseDbTest with ScalaFutures {

  private implicit val system = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  "Users service" should {

    "retrieve users list" in {
      val users: Future[Seq[UserEntity]] = UsersService.getUsers()
      val userEntities: Seq[UserEntity] = Await.result(users, 10.seconds)
      log.info(s"users: $userEntities")
    }

    "retrieve users stream" in {
      val users: DatabasePublisher[UserEntity] = UsersService.getUsersStream()

      val printFuture = Source(users).runForeach( u => log.info(s"user: $u"))

      Await.result(printFuture, 10.seconds)
    }

    "retrieve and test async" in {
      val userNamesFuture: Future[Seq[String]] = UsersService.getPlain()
      userNamesFuture map { u => u should have size 3 }
    }

    "retrieve by plain sql list" in {
      val userNamesFuture: Future[Seq[String]] = UsersService.getPlain()
      val userNames: Seq[String] = Await.result(userNamesFuture, 10.seconds)
      log.info(s"users: $userNames")
    }

    "retrieve by plain sql list 2" in {
      val userNamesFuture: Future[Seq[String]] = UsersService.getPlain()

      userNamesFuture.onComplete {
        _ match {
          case Success(n) => println(s"complete. user names: $n")
          case Failure(ex) => println(s"complete. failed to get user names because of $ex")
        }
      }

      // TODO - test recovery with retry - recursion
//      userNamesFuture.recoverWith()

      Await.result(userNamesFuture, 10.seconds)
    }
  }

  "retrieve user by id" in {
    //      Get("/users/1") ~> usersRoute ~> check {
    //        responseAs[JsObject] should be(testUsers.head.toJson)
    //      }
  }

  "update user by id and retrieve it" in {
    //      val newUsername = "UpdatedUsername"
    //      val requestEntity = HttpEntity(MediaTypes.`application/json`, JsObject("username" -> JsString(newUsername)).toString())
    //      Post("/users/1", requestEntity) ~> usersRoute ~> check {
    //        responseAs[JsObject] should be(testUsers.head.copy(username = newUsername).toJson)
    //        whenReady(getUserById(1)) { result =>
    //          result.get.username should be(newUsername)
    //        }
    //      }
  }

  "delete user" in {
    //      Delete("/users/3") ~> usersRoute ~> check {
    //        response.status should be(NoContent)
    //        whenReady(getUserById(3)) { result =>
    //          result should be(None: Option[UserEntity])
    //        }
    //      }
  }

  "retrieve currently logged user" in {
    //      Get("/users/me") ~> addHeader("Token", testTokens.find(_.userId.contains(2)).get.token) ~> usersRoute ~> check {
    //        responseAs[JsObject] should be(testUsers.find(_.id.contains(2)).get.toJson)
    //      }
  }

  "update currently logged user" in {
    //      val newUsername = "MeUpdatedUsername"
    //      val requestEntity = HttpEntity(MediaTypes.`application/json`, JsObject("username" -> JsString(newUsername)).toString())
    //      Post("/users/me", requestEntity) ~> addHeader("Token", testTokens.find(_.userId.contains(2)).get.token) ~> usersRoute ~> check {
    //        responseAs[JsObject] should be(testUsers.find(_.id.contains(2)).get.copy(username = newUsername).toJson)
    //        whenReady(getUserById(2)) { result =>
    //          result.get.username should be(newUsername)
    //        }
    //      }
    //    }
  }

}
