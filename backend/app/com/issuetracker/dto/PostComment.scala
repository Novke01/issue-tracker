package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.Comment

case class PostComment(
    content: String,
    userId: Long,
    userUsername: String,
    issueId: Option[Long],
    pullRequestId: Option[Long]
)

object PostComment {

  implicit val postCommentReads: Reads[PostComment] = Json.reads[PostComment]

  implicit def GetCommentToComment(postComment: PostComment): Comment =
    Comment(
      -1,
      postComment.content,
      postComment.userId,
      postComment.userUsername,
      postComment.issueId,
      postComment.pullRequestId
    )
}
