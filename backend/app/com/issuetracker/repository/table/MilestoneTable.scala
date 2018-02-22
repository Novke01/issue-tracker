package com.issuetracker.repository.table

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.model.Milestone
import org.joda.time.DateTime
import com.github.tototoshi.slick.PostgresJodaSupport._

class MilestoneTable(tag: Tag) extends Table[Milestone](tag, "milestones") {

  def id           = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title        = column[String]("title")
  def description  = column[String]("description")
  def dueDate      = column[DateTime]("dueDate")
  def repositoryId = column[Long]("repositoryId")

  def repository =
    foreignKey("repository_FK", repositoryId, RepositoryTable.repositories)(
      _.id,
      onDelete = ForeignKeyAction.Cascade)

  def * = (id, title, description, dueDate, repositoryId) <> (Milestone.tupled, Milestone.unapply)

}

object MilestoneTable {

  lazy val milestones = TableQuery[MilestoneTable]

}
