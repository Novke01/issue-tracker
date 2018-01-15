package com.issuetracker.repository.table

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.model.Repository

class RepositoryTable(tag: Tag) extends Table[Repository](tag, "repositories") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def url = column[String]("url", O.Unique)
  def description = column[String]("description")
  def ownerId = column[Long]("ownerId")
  def owner = foreignKey("owner_FK", ownerId, UserTable.users)(_.id)
  
  def * = (id, name, url, description, ownerId) <> (Repository.tupled, Repository.unapply)

}

object RepositoryTable {
  
  lazy val repositories = TableQuery[RepositoryTable]
  
}