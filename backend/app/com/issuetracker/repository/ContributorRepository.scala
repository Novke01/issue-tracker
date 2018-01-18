package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.{Contributor, User}
import com.issuetracker.repository.table.ContributorTable
import slick.jdbc.PostgresProfile.api._
import com.issuetracker.repository.table.RepositoryTable
import com.issuetracker.repository.table.UserTable

class ContributorRepository(db: Database) {

  lazy val contributors = ContributorTable.contributors
  lazy val users = UserTable.users
  lazy val repositories = RepositoryTable.repositories

  def create(): Future[Unit] = db.run(contributors.schema.create)

  def insert(contributor: Contributor): Future[Contributor] = db.run((contributors returning contributors) += contributor)

  def addContributors(repoId: Long, contributorIds: Seq[Long]) = db.run({
    contributors ++= contributorIds.map(Contributor(-1, _, repoId))
  })

  def getContributorsByRepositoryId(repoId: Long): Future[Seq[User]] = db.run({
    for {
      c <- contributors.filter(_.repositoryId === repoId)
      user <- users.filter(_.id === c.userId)
    } yield (user)
  }.result)
}

object ContributorRepository {
  
  def apply(db: Database): ContributorRepository = 
    new ContributorRepository(db)
      
}
