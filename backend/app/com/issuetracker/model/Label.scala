package com.issuetracker.model

case class Label(
    id: Long,
    name: String,
    color: String,
    repositoryId: Long
)
