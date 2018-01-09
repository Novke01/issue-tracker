package com.issuetracker.dto

import play.api.libs.json._

case class RefreshToken(
  token: String
)

object RefreshToken {
  implicit val refreshTokenReads = Json.reads[RefreshToken]
}