package com.issuetracker.dto

import com.issuetracker.model.User
import play.api.libs.json._

case class GetUser(
                    id: Long,
                    username: String,
                    firstName: String,
                    lastName: String,
                    email: String
                   )

object GetUser {

  implicit val getUserWrites = Json.writes[GetUser]

  implicit def userToGetUser(user: User): GetUser =
    new GetUser(
      user.id,
      user.username,
      user.firstName,
      user.lastName,
      user.email
    )

}
