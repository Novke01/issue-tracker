package com.issuetracker.dto

import play.api.libs.json._
import play.api.libs.functional.syntax._
import com.issuetracker.model.User

case class RegisteredUser(
    id: Long,
    username: String,
    firstName: String,
    lastName: String,
    email: String
)

object RegisteredUser {
  
  implicit val registeredWrites: Writes[RegisteredUser] = (
    (JsPath \ "id").write[Long] and
    (JsPath \ "username").write[String] and
    (JsPath \ "firstName").write[String] and
    (JsPath \ "lastName").write[String] and
    (JsPath \ "email").write[String]
  )(unlift(RegisteredUser.unapply))
  
  implicit def userToRegisteredUser(user: User): RegisteredUser =
    new RegisteredUser(
      user.id,
      user.username,
      user.firstName,
      user.lastName,
      user.email
    )
  
}