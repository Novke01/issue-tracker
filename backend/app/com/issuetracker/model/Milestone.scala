package com.issuetracker.model

import org.joda.time.DateTime

case class Milestone(
    id: Long,
    title: String,
    description: String,
    dueDate: DateTime,
    repositoryId: Long
)
