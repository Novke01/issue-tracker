package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.{Contributor, Repository}
import slick.jdbc.PostgresProfile.api._

class ContributorRepository(db: Database, val userRepository: UserRepository, val repositoryRepository: RepositoryRepository) {

  lazy val contributors = TableQuery[ContributorTable]
  lazy val users = userRepository.users
  lazy val repositories = repositoryRepository.repositories

  class ContributorTable(tag: Tag) extends Table[Contributor](tag, "contributors") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userId = column[Long]("userId")

    def repositoryId = column[Long]("repositoryId")

    def user = foreignKey("user_FK", userId, users)(_.id)

    def repository = foreignKey("repository_FK", repositoryId, repositories)(_.id)

    def * = (id, userId, repositoryId) <> (Contributor.tupled, Contributor.unapply)

  }

  def create(): Future[Unit] = db.run(contributors.schema.create)

  def insert(contributor: Contributor): Future[Contributor] = db.run((contributors returning contributors) += contributor)

  def findRepositoryByContributorId(id: Long): Future[Seq[Repository]] = db.run({
    for {
      (repository, _) <- repositories join contributors.filter(_.userId === id) on (_.id === _.repositoryId)
    } yield (repository)
  }.result
  )

  def insertContributors(repoId: Long, contributorIds: Seq[Long]) = db.run({
    contributors ++= contributorIds.map(Contributor(-1, _, repoId))
  })

}