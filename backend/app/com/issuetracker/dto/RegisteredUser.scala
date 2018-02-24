package com.issuetracker.dto

import com.issuetracker.model.User
import play.api.libs.json.{Json, OWrites}

case class RegisteredUser(
    id: Long,
    username: String,
    firstName: String,
    lastName: String,
    email: String
)

object RegisteredUser {

  implicit val registeredUserWrites: OWrites[RegisteredUser] = Json.writes[RegisteredUser]

  implicit def userToRegisteredUser(user: User): RegisteredUser =
    new RegisteredUser(
      user.id,
      user.username,
      user.firstName,
      user.lastName,
      user.email
    )

}
