package com.issuetracker.repository.table

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.model.PullRequest

class PullRequestTable(tag: Tag) extends Table[PullRequest](tag, "pullrequests") {

  def id           = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title        = column[String]("title")
  def url          = column[String]("url")
  def repositoryId = column[Long]("repositoryId")

  def repository =
    foreignKey("repository_FK", repositoryId, RepositoryTable.repositories)(
      _.id,
      onDelete = ForeignKeyAction.Cascade)

  def * = (id, title, url, repositoryId) <> (PullRequest.tupled, PullRequest.unapply)

}

object PullRequestTable {

  lazy val pullRequests = TableQuery[PullRequestTable]

}
