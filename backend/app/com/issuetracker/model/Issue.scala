package com.issuetracker.model

import com.issuetracker.model.IssueStatus._

case class Issue(
    id: Long,
    repositoryId: Long,
    title: String,
    description: String,
    created: Long,
    ownerId: Long,
    status: IssueStatus,
    milestoneId: Option[Long]
    //labels: List<Label>
    //history: List<Item>*/
)
