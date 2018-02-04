package com.issuetracker.dto

import com.issuetracker.model.IssueStatus.IssueStatus
import play.api.libs.json._
import com.issuetracker.model.{Issue, IssueStatus}

case class UpdateIssue(
    id: Long,
    repositoryId: Long,
    title: String,
    description: String,
    created: Long,
    ownerId: Long,
    status: IssueStatus
)
object UpdateIssue {

  implicit val updateIssueReads = Json.reads[UpdateIssue]

  implicit def updateIssueToIssue(updateIssue: UpdateIssue): Issue =
    new Issue(
      updateIssue.id,
      updateIssue.repositoryId,
      updateIssue.title,
      updateIssue.description,
      updateIssue.created,
      updateIssue.ownerId,
      updateIssue.status
    )

}
