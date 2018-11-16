package com.strawpay.issuer.route
import akka.http.scaladsl.server.{Directives, Route}

/**
  *
  */
trait AssetsRoute extends Directives {
  def assetsRoute: Route = pathPrefix("assets" / Remaining) { file =>
    // optionally compresses the response with Gzip or Deflate
    // if the client accepts compressed responses
    encodeResponse {
      getFromResource("public/" + file)
    }
  }
}
