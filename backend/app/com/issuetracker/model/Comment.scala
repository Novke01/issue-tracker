package com.issuetracker.model

case class Comment(
    id: Long,
    content: String,
    userId: Long,
    userUsername: String,
    issueId: Option[Long],
    pullRequestId: Option[Long]
)
