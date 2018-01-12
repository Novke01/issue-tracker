package com.issuetracker.repository

import scala.concurrent.Future

import com.issuetracker.model.Repository

import slick.jdbc.PostgresProfile.api.Database
import slick.jdbc.PostgresProfile.api.Table
import slick.jdbc.PostgresProfile.api.TableQuery
import slick.jdbc.PostgresProfile.api.Tag
import slick.jdbc.PostgresProfile.api.anyToShapedValue
import slick.jdbc.PostgresProfile.api.columnExtensionMethods
import slick.jdbc.PostgresProfile.api.longColumnType
import slick.jdbc.PostgresProfile.api.queryInsertActionExtensionMethods
import slick.jdbc.PostgresProfile.api.schemaActionExtensionMethods
import slick.jdbc.PostgresProfile.api.streamableQueryActionExtensionMethods
import slick.jdbc.PostgresProfile.api.stringColumnType
import slick.jdbc.PostgresProfile.api.tableQueryToTableQueryExtensionMethods
import slick.jdbc.PostgresProfile.api.valueToConstColumn

class RepositoryRepository(db: Database, val userRepository: UserRepository) {

  lazy val repositories = TableQuery[RepositoryTable]
  lazy val users = userRepository.users

  class RepositoryTable(tag: Tag) extends Table[Repository](tag, "repositories") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def url = column[String]("url")

    def description = column[String]("description")

    def ownerId = column[Long]("ownerId")

    def owner = foreignKey("owner_FK", ownerId, users)(_.id)

    def * = (id, name, url, description, ownerId) <> (Repository.tupled, Repository.unapply)

  }

  def create(): Future[Unit] = db.run(repositories.schema.create)

  def insert(repository: Repository): Future[Repository] = db.run((repositories returning repositories) += repository)

  def findByOwnerId(id: Long): Future[Seq[Repository]] = {
    db.run(repositories.filter(_.ownerId === id).result)
  }

}