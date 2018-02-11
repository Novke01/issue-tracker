package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.{Contributor, User}
import com.issuetracker.repository.table.ContributorTable
import slick.jdbc.PostgresProfile.api._
import com.issuetracker.repository.table.RepositoryTable
import com.issuetracker.repository.table.UserTable

class ContributorRepository(db: Database) {

  lazy val contributors = ContributorTable.contributors
  lazy val users        = UserTable.users
  lazy val repositories = RepositoryTable.repositories

  def create(): Future[Unit] = db.run(contributors.schema.create)

  def insert(contributor: Contributor): Future[Contributor] =
    db.run((contributors returning contributors) += contributor)

  def addContributors(repoId: Long, contributorIds: Seq[Long]): Future[Option[Int]] =
    db.run({
      contributors ++= contributorIds.map(Contributor(-1, _, repoId))
    })

  //finds all contributors (and owner) of repository with id repoId, whose firstName, lastName or username contain searchTerm
  def findByRepositoryIdAndSearchTerm(repoId: Long, searchTerm: String): Future[Seq[User]] = {
    val searchTermQuery = s"%$searchTerm%".toLowerCase
    db.run({

      for {
        (c, r) <- contributors join repositories
          .filter(_.id === repoId) on (_.repositoryId === _.id)
        user <- users.filter(u =>
          ((u.id === c.userId) || u.id === r.ownerId) &&
            ((u.firstName.toLowerCase like searchTermQuery) || (u.lastName.toLowerCase like searchTermQuery) || (u.username.toLowerCase like searchTermQuery)))
      } yield user
    }.result)
  }

  def getContributorsByRepositoryId(repoId: Long): Future[Seq[User]] =
    db.run({
      for {
        c    <- contributors.filter(_.repositoryId === repoId)
        user <- users.filter(_.id === c.userId)
      } yield user
    }.result)
}

object ContributorRepository {

  def apply(db: Database): ContributorRepository =
    new ContributorRepository(db)

}
