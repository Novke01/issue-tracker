package com.issuetracker.exception

class IncorrectPasswordException(
  private val message: String = "Password didn't match with real password."
) extends Exception(message)