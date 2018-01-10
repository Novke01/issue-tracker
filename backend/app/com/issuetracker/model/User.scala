package com.issuetracker.model

case class User(
  val id: Long,
  val username: String,
  val password: String,
  val firstName: String,
  val lastName: String,
  val email: String,
  val refreshToken: String
)