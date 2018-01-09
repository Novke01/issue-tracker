package com.issuetracker.filter

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Success

import akka.stream.Materializer
import pdi.jwt.JwtAlgorithm
import pdi.jwt.JwtJson
import play.api.Configuration
import play.api.mvc.Filter
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import play.api.mvc.Results._
import play.api.routing.Router
import play.api.libs.json.JsError


class JwtFilter(secret: String)(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    
    val handler = requestHeader.attrs(Router.Attrs.HandlerDef)
    val modifiers = handler.modifiers
    
    if (modifiers.contains("noauth")) {
      nextFilter(requestHeader)
    } else if (requestHeader.headers.hasHeader("Authorization")) {
      val authOption = requestHeader.headers.get("Authorization")
      authOption match {
        case Some(auth) =>
          val token = auth.stripPrefix("Bearer").trim
          val algo = JwtAlgorithm.HS256
          if (JwtJson.isValid(token, secret, Seq(algo))) {
            nextFilter(requestHeader)
          } else {
            Future { Unauthorized("You are not logged in.") }
          }
        case None =>
          Future { Unauthorized("You are not logged in.") }
      }
    } else {
      Future { Unauthorized("You are not logged in.") }
    }
  }
  
}

object JwtFilter {
  
  def apply(configuration: Configuration)(implicit mat: Materializer, ec: ExecutionContext): JwtFilter = {
    val secret = configuration.get[String]("jwt.secret")
    new JwtFilter(secret)
  }
  
}