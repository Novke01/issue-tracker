package com.issuetracker.dto

import play.api.libs.json.{Json, Reads}

case class RefreshToken(
    token: String
)

object RefreshToken {
  implicit val refreshTokenReads: Reads[RefreshToken] = Json.reads[RefreshToken]
}
