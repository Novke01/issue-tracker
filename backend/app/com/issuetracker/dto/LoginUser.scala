package com.issuetracker.dto

import play.api.libs.json.{Json, Reads}

case class LoginUser(
    username: String,
    password: String
)

object LoginUser {
  implicit val loginUserReads: Reads[LoginUser] = Json.reads[LoginUser]
}
