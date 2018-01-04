package com.issuetracker.dto

import play.api.libs.json._
import play.api.libs.functional.syntax._
import com.issuetracker.model.User

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
      registerUser.email
    )
  
}