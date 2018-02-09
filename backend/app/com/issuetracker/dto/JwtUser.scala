package com.issuetracker.dto

import com.github.nscala_time.time.Imports.DateTime
import com.github.nscala_time.time.Imports.richAbstractInstant
import com.github.nscala_time.time.Imports.richDateTime
import com.github.nscala_time.time.Imports.richInt
import com.github.nscala_time.time.Imports.richReadableInstant
import com.issuetracker.model.User

import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.typedmap.TypedKey

case class JwtUser(
  id: Long,
  username: String,
  firstName: String,
  lastName: String,
  email: String,
  exp: Long
)

object JwtUser {

  implicit val jwtUserFormat: OFormat[JwtUser] = Json.format[JwtUser]

  implicit def userToJwtUser(user: User): JwtUser = JwtUser(
    user.id,
    user.username,
    user.firstName,
    user.lastName,
    user.email,
    (DateTime.now + 1.day).instant.millis / 1000
  )
  
  val Key: TypedKey[JwtUser] = TypedKey[JwtUser]
  
}
