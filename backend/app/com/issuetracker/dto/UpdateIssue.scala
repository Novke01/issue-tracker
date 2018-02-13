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
    status: IssueStatus,
    milestoneId: Option[Long]
)
object UpdateIssue {

  implicit val updateIssueReads: Reads[UpdateIssue] = Json.reads[UpdateIssue]

  implicit def updateIssueToIssue(updateIssue: UpdateIssue): Issue =
    Issue(
      updateIssue.id,
      updateIssue.repositoryId,
      updateIssue.title,
      updateIssue.description,
      updateIssue.created,
      updateIssue.ownerId,
      updateIssue.status,
      updateIssue.milestoneId
    )

}
