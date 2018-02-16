package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.Comment

case class GetComment(
    id: Long,
    content: String,
    userId: Long,
    userUsername: String,
    issueId: Option[Long],
    pullRequestId: Option[Long]
)

object GetComment {

  implicit val getCommentWrites = Json.writes[GetComment]

  implicit def commentToGetComment(comment: Comment): GetComment =
    new GetComment(
      comment.id,
      comment.content,
      comment.userId,
      comment.userUsername,
      comment.issueId,
      comment.pullRequestId
    )

}
