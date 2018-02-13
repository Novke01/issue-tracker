package com.issuetracker.dto

import com.issuetracker.model.User

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.JsPath
import play.api.libs.json.Reads

case class RegisterUser(
    username: String,
    password: String,
    firstName: String,
    lastName: String,
    email: String
)

object RegisterUser {

  implicit val registerReads: Reads[RegisterUser] = (
    (JsPath \ "username").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "email").read[String](Reads.email)
  )(RegisterUser.apply _)

  implicit def registerUserToUser(registerUser: RegisterUser): User =
    new User(
      -1,
      registerUser.username,
      registerUser.password,
      registerUser.firstName,
      registerUser.lastName,
      registerUser.email,
      ""
    )

}
