package com.strawpay
package issuer

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import com.strawpay.issuer.api.{Account, Note, RegisterForm}
import com.strawpay.issuer.shared.SharedMessages
import com.strawpay.issuer.twirl.Implicits._
import de.heikoseeberger.akkahttpupickle.UpickleSupport

class WebService() extends Directives with UpickleSupport {

  val apiRoute = pathSingleSlash {
    get {
      complete {
        com.strawpay.issuer.html.index.render(SharedMessages.itWorks)
      }
    }
  } ~ pathPrefix("api") {
    pathPrefix("register") {
      get {
        complete {
          RegisterForm.template
        }
      } ~
        post {
          decodeRequest {
            entity(as[RegisterForm]) {f ⇒ f.validate match {
                case Right(form) ⇒  complete(Account("N/A" + form))
                case Left(error) ⇒  complete(StatusCodes.BadRequest, error)
              }
            }
          }
        }

    } ~ path("acquire") {
      post {
        complete {
          Note("fake")
        }
      }
    }
  } ~ pathPrefix("assets" / Remaining) { file =>
    // optionally compresses the response with Gzip or Deflate
    // if the client accepts compressed responses
    encodeResponse {
      getFromResource("public/" + file)
    }
  }
}
