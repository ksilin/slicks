package me.archdev

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import me.archdev.restapi.models.UserEntity
import me.archdev.restapi.services.UsersService
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import scala.concurrent.Await
import scala.concurrent.duration._
import util.Failure

import scala.concurrent.Future

class UsersServiceRoutlessTest extends BaseServiceTest with ScalaFutures {

  "Users service" should {

    "retrieve users list" in {
      val users: Future[Seq[UserEntity]] = UsersService.getUsers()
      val userEntities: Seq[UserEntity] = Await.result(users, 10.seconds)
        log.info(s"users: $userEntities")
        println(s"users: $userEntities")
      }

    "retrieve by plain sql list" in {
      val userNamesFuture: Future[Seq[String]] = UsersService.getPlain()
      val userNames: Seq[String] = Await.result(userNamesFuture, 10.seconds)
        log.info(s"users: $userNames")
        println(s"users: $userNames")
      }

    "retrieve by plain sql list 2" in {
      val userNamesFuture: Future[Seq[String]] = UsersService.getPlain()

      userNamesFuture.onComplete {
        _ match {
        case Success(n) => println(s"user names: $n")
        case Failure(ex) => println(s"failes to get user names because of $ex")
        }
      }

      val userNames: Seq[String] = Await.result(userNamesFuture, 10.seconds)
        log.info(s"users: $userNames")
        println(s"users: $userNames")
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
