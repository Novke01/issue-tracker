package com.issuetracker.exception

class IssueNotFoundException(
    private val message: String = "Issue doesn't exist."
) extends Exception(message)
