package com.issuetracker.dto

import play.api.libs.json._

case class LoggedInUser(
  accessToken: String,
  refreshToken: String
)

object LoggedInUser {
  implicit val loggedInUserWrites = Json.writes[LoggedInUser]
}