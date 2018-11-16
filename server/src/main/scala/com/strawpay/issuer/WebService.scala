package com.strawpay
package issuer

import akka.http.scaladsl.server.{Directives, Route}
import com.strawpay.issuer.route.{ApiRoute, AssetsRoute}

class WebService() extends Directives with ApiRoute with AssetsRoute {
  val route: Route = apiRoute ~ assetsRoute
}
