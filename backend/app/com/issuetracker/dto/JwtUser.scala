package com.issuetracker.dto

import com.github.nscala_time.time.Imports._
import com.issuetracker.model.User

import play.api.libs.json._
import org.joda.time.DateTimeUtils

case class JwtUser(
    id: Long,
    username: String,
    firstName: String,
    lastName: String,
    email: String,
    exp: Long
)

object JwtUser {
  
//  implicit val jodaDateWrites: Writes[DateTime] = new Writes[DateTime] {
//    def writes(d: DateTime): JsValue = JsNumber(d.)
//  }
  
  implicit val jwtUserWrites: OWrites[JwtUser] = Json.writes[JwtUser]
  
  implicit def userToJwtUser(user: User): JwtUser = JwtUser(
    user.id,
    user.username,
    user.firstName,
    user.lastName,
    user.email,
    (DateTime.now + 10.seconds).instant.millis / 1000
  )
  
}