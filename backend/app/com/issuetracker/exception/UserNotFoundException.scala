package com.issuetracker.exception

class UserNotFoundException(
  private val message: String = "User doesn't exist."
) extends Exception(message)