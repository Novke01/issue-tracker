package com.issuetracker.model

case class WikiPage(
    id: Long,
    name: String,
    content: String,
    repositoryId: Long
)
