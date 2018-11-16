package com.strawpay.issuer.route
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.strawpay.issuer.api.{Account, RegisterForm}
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

/**
  *
  */
class ApiRouteSpec extends WordSpec with ApiRoute with Matchers with ScalatestRouteTest
with ScalaFutures with UpickleSupport {
  "The ApiRoute" should {

    "Do not handle call to root" in {
      // tests:
      Get() ~> apiRoute ~> check {
        handled shouldBe false
      }
    }

    "return a 'registerForm' response for GET requests to /api/register" in {
      // tests:
      Get("/api/register") ~> apiRoute ~> check {
        responseAs[RegisterForm] shouldEqual RegisterForm.template
      }
    }

    "return an BadRequest response on an invalid register request" in {
      Post("/api/register", "invalid") ~> Route.seal(apiRoute) ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "return an 'account' response on a valid register request" in {
      val form = RegisterForm("phoneNumber", "email")
      Post("/api/register", form) ~> apiRoute ~> check {
        responseAs[Account] shouldEqual Account("N/A" + form)
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      // tests:
      Put("/api/register") ~> Route.seal(apiRoute) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
      }
    }
  }
}
