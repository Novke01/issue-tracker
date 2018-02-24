package com.issuetracker.repository.table

import com.issuetracker.model.{AssignedUser, IssueLabel}
import slick.jdbc.PostgresProfile.api._

class IssueLabelTable(tag: Tag) extends Table[IssueLabel](tag, "issueLabels") {

  def id      = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def labelId = column[Long]("labelId")
  def issueId = column[Long]("issueId")
  def label =
    foreignKey("label_FK", labelId, LabelTable.labels)(_.id, onDelete = ForeignKeyAction.Cascade)
  def issue =
    foreignKey("issue_FK", issueId, IssueTable.issues)(_.id, onDelete = ForeignKeyAction.Cascade)

  def * = (id, labelId, issueId) <> (IssueLabel.tupled, IssueLabel.unapply)

}

object IssueLabelTable {

  lazy val issueLabels = TableQuery[IssueLabelTable]

}
