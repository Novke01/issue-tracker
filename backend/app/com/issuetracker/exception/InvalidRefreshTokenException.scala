package com.issuetracker.exception

class InvalidRefreshTokenException(
    private val message: String = "Invalid refresh token."
) extends Exception(message)
