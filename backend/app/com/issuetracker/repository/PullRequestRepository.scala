package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.PullRequest
import slick.jdbc.PostgresProfile.api._
import com.issuetracker.repository.table.PullRequestTable

class PullRequestRepository(db: Database) {

  lazy val pullRequests = PullRequestTable.pullRequests

  def create(): Future[Unit] = db.run(pullRequests.schema.create)

  def drop(): Future[Unit] = db.run(pullRequests.schema.drop)

  def insert(pullRequest: PullRequest): Future[PullRequest] =
    db.run((pullRequests returning pullRequests) += pullRequest)

  def get(id: Long): Future[Option[PullRequest]] =
    db.run(pullRequests.filter(_.id === id).result.headOption)

  def delete(id: Long): Future[Int] =
    db.run(pullRequests.filter(_.id === id).delete)

  def getByRepositoryId(id: Long): Future[Seq[PullRequest]] =
    db.run(pullRequests.filter(_.repositoryId === id).result)
}

object PullRequestRepository {

  def apply(db: Database): PullRequestRepository =
    new PullRequestRepository(db)

}
