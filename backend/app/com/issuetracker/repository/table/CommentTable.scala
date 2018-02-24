package com.issuetracker.repository.table

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.model.Comment

class CommentTable(tag: Tag) extends Table[Comment](tag, "comments") {

  def id            = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def content       = column[String]("content")
  def userId        = column[Long]("user_id")
  def userUsername  = column[String]("user_username")
  def issueId       = column[Option[Long]]("issue_id")
  def pullRequestId = column[Option[Long]]("pull_request_id")

  def user = foreignKey("user_FK", userId, UserTable.users)(_.id)
  def issue =
    foreignKey("issue_FK", issueId, IssueTable.issues)(_.id, onDelete = ForeignKeyAction.Cascade)
  def pullRequest =
    foreignKey("pull_request_FK", pullRequestId, PullRequestTable.pullRequests)(
      _.id,
      onDelete = ForeignKeyAction.Cascade)

  def * =
    (id, content, userId, userUsername, issueId, pullRequestId) <> (Comment.tupled, Comment.unapply)

}

object CommentTable {

  lazy val comments = TableQuery[CommentTable]

}
