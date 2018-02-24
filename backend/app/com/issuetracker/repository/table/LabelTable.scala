package com.issuetracker.repository.table

import com.issuetracker.model.Label
import slick.jdbc.PostgresProfile.api._

class LabelTable(tag: Tag) extends Table[Label](tag, "labels") {

  def id           = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name         = column[String]("name")
  def color        = column[String]("color")
  def repositoryId = column[Long]("repositoryId")
  def repository =
    foreignKey("repository_FK", repositoryId, RepositoryTable.repositories)(
      _.id,
      onDelete = ForeignKeyAction.Cascade)

  def * =
    (id, name, color, repositoryId) <> (Label.tupled, Label.unapply)

}

object LabelTable {

  lazy val labels = TableQuery[LabelTable]

}
