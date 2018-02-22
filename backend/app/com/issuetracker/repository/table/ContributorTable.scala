package com.issuetracker.repository.table

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.model.Contributor

class ContributorTable(tag: Tag) extends Table[Contributor](tag, "contributors") {

  def id           = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId       = column[Long]("userId")
  def repositoryId = column[Long]("repositoryId")
  def user         = foreignKey("user_FK", userId, UserTable.users)(_.id)
  def repository =
    foreignKey("repository_FK", repositoryId, RepositoryTable.repositories)(
      _.id,
      onDelete = ForeignKeyAction.Cascade)

  def * = (id, userId, repositoryId) <> (Contributor.tupled, Contributor.unapply)

}

object ContributorTable {

  lazy val contributors = TableQuery[ContributorTable]

}
