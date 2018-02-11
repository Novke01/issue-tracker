package com.issuetracker.dto

import java.lang.System.currentTimeMillis

import play.api.libs.json._
import com.issuetracker.model.{Issue, IssueStatus}

case class PostIssue(
    repositoryId: Long,
    title: String,
    description: String,
    ownerId: Long,
    assignees: List[Long],
    labels: List[Long]
)
object PostIssue {

  implicit val postIssueReads: Reads[PostIssue] = Json.reads[PostIssue]

  implicit def postIssueToIssue(postIssue: PostIssue): Issue =
    Issue(
      -1,
      postIssue.repositoryId,
      postIssue.title,
      postIssue.description,
      currentTimeMillis(),
      postIssue.ownerId,
      IssueStatus.OPENED
    )

}
