package com.issuetracker.repository.table

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.model.Issue
import com.issuetracker.model.IssueStatus.IssueStatus

class IssueTable(tag: Tag) extends Table[Issue](tag, "issues") {

  def id           = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def repositoryId = column[Long]("repositoryId")
  def repository =
    foreignKey("repository_FK", repositoryId, RepositoryTable.repositories)(
      _.id,
      onDelete = ForeignKeyAction.Cascade)
  def title       = column[String]("title")
  def description = column[String]("description")
  def created     = column[Long]("created")
  def status      = column[IssueStatus]("status")
  def ownerId     = column[Long]("ownerId")
  def milestoneId = column[Option[Long]]("milestoneId")

  def owner = foreignKey("owner_FK", ownerId, UserTable.users)(_.id)
  def milestone =
    foreignKey("milestone_FK", milestoneId, MilestoneTable.milestones)(_.id,
                                                                       onDelete =
                                                                         ForeignKeyAction.SetNull)

  def * =
    (id, repositoryId, title, description, created, ownerId, status, milestoneId) <> (Issue.tupled, Issue.unapply)

}

object IssueTable {

  lazy val issues = TableQuery[IssueTable]

}
