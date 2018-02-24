package com.issuetracker.model

case class PullRequest(
    id: Long,
    title: String,
    url: String,
    repositoryId: Long
)
