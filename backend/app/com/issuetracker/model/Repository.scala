package com.issuetracker.model

case class Repository(
    id: Long,
    name: String,
    url: String,
    description: String,
    ownerId: Long
)
