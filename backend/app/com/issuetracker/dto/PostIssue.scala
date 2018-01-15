package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.Issue
import com.issuetracker.model.IssueStatus._

case class PostIssue(
                     title: String,
                     description: String,
                     created: Long,
                     ownerId: Long,
                     status: IssueStatus,
                     assignees: List[Long]

                   )
object PostIssue {

  implicit val repositoryReads = Json.reads[PostIssue]

  implicit def postIssueToIssue(newIssue: PostIssue): Issue =
    new Issue(
      -1,
      newIssue.title,
      newIssue.description,
      newIssue.created,
      newIssue.ownerId,
      newIssue.status
    )

}