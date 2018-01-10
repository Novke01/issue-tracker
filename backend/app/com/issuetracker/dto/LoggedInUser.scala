package com.issuetracker.dto

import play.api.libs.json.Json

case class LoggedInUser(
  accessToken: String,
  refreshToken: String
)

object LoggedInUser {
  implicit val loggedInUserWrites = Json.writes[LoggedInUser]
}