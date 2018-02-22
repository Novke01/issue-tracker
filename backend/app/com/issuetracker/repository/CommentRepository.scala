package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.Comment
import slick.jdbc.PostgresProfile.api._
import com.issuetracker.repository.table.CommentTable

class CommentRepository(db: Database) {

  lazy val comments = CommentTable.comments

  def create(): Future[Unit] = db.run(comments.schema.create)

  def drop(): Future[Unit] = db.run(comments.schema.drop)

  def insert(comment: Comment): Future[Comment] =
    db.run((comments returning comments) += comment)

  def get(id: Long): Future[Option[Comment]] =
    db.run(comments.filter(_.id === id).result.headOption)

  def getByIssueId(id: Long): Future[Seq[Comment]] =
    db.run(comments.filter(_.issueId === id).result)

  def getByPullRequestId(id: Long): Future[Seq[Comment]] =
    db.run(comments.filter(_.pullRequestId === id).result)
}

object CommentRepository {

  def apply(db: Database): CommentRepository =
    new CommentRepository(db)

}
