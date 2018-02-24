package com.issuetracker.dto

import play.api.libs.json.{Json, OWrites}

case class LoggedInUser(
    accessToken: String,
    refreshToken: String
)

object LoggedInUser {
  implicit val loggedInUserWrites: OWrites[LoggedInUser] = Json.writes[LoggedInUser]
}
