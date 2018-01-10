package com.issuetracker.dto

import play.api.libs.json.Json

case class LoginUser(
  username: String,
  password: String
)

object LoginUser {
  implicit val loginUserReads = Json.reads[LoginUser]
}