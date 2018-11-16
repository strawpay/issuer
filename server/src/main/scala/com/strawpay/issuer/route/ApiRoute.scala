package com.strawpay.issuer.route
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import com.strawpay.issuer.api.{Account, Note, RegisterForm}
import de.heikoseeberger.akkahttpupickle.UpickleSupport

trait ApiRoute extends Directives with UpickleSupport {

  def apiRoute: Route = pathPrefix("api") {
    pathPrefix("register") {
      get {
        complete {
          RegisterForm.template
        }
      } ~
        post {
          decodeRequest {
            entity(as[RegisterForm]) {
              _.validate match {
                case Right(form) ⇒ complete(Account("N/A" + form))
                case Left(error) ⇒ complete(StatusCodes.BadRequest, error)
              }
            }
          }
        }

    } ~
      path("acquire") {
        post {
          complete {
            Note("fake")
          }
        }
      }
  }
}
