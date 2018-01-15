package com.issuetracker.repository

import scala.concurrent.Future

import com.issuetracker.model.Repository

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.repository.table.RepositoryTable
import com.issuetracker.repository.table.ContributorTable
import com.issuetracker.repository.table.UserTable

class RepositoryRepository(db: Database) {

  lazy val repositories = RepositoryTable.repositories
  lazy val contributors = ContributorTable.contributors
  lazy val users = UserTable.users

  def create(): Future[Unit] = db.run(repositories.schema.create)

  def insert(repository: Repository): Future[Repository] = 
    db.run((repositories returning repositories) += repository)

  def findByOwnerId(id: Long): Future[Seq[Repository]] =
    db.run(repositories.filter(_.ownerId === id).result)
    
  def findByContributorId(id: Long): Future[Seq[Repository]] = db.run({
    for {
      (repository, _) <- repositories join contributors.filter(_.userId === id) on (_.id === _.repositoryId)
    } yield (repository)
  }.result)

}

object RepositoryRepository {
  
  def apply(db: Database): RepositoryRepository = 
    new RepositoryRepository(db)
  
}
