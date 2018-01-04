package com.issuetracker.model

case class User(
  id: Long,
  username: String,
  password: String,
  firstName: String,
  lastName: String,
  email: String
)