package com.issuetracker.service

import com.issuetracker.dto.{GetComment, PostComment}

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.repository.CommentRepository

class CommentService(
    val commentRepository: CommentRepository
)(implicit val ec: ExecutionContext) {

  def insert(pullRequest: PostComment): Future[GetComment] = {
    commentRepository.insert(pullRequest).map(GetComment.commentToGetComment)
  }

  def get(id: Long): Future[Option[GetComment]] = {
    commentRepository.get(id).map(_.map(GetComment.commentToGetComment))
  }

  def getByIssueId(id: Long): Future[Seq[GetComment]] = {
    commentRepository
      .getByIssueId(id)
      .map(_.map(GetComment.commentToGetComment))
  }

  def getByPullRequestId(id: Long): Future[Seq[GetComment]] = {
    commentRepository
      .getByPullRequestId(id)
      .map(_.map(GetComment.commentToGetComment))
  }
}

object CommentService {

  def apply(commentRepository: CommentRepository)(implicit ec: ExecutionContext): CommentService =
    new CommentService(commentRepository)

}
