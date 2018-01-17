package com.issuetracker.repository.table

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.model.Issue
import com.issuetracker.model.IssueStatus.IssueStatus

class IssueTable(tag: Tag) extends Table[Issue](tag, "issues") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def description = column[String]("description")
  def created = column[Long]("created")
  def status = column[IssueStatus]("status")
  def ownerId = column[Long]("ownerId")
  def owner = foreignKey("owner_FK", ownerId, UserTable.users)(_.id)

  def * = (id, title, description, created, ownerId, status) <> (Issue.tupled, Issue.unapply)

}

object IssueTable {

  lazy val issues = TableQuery[IssueTable]

}