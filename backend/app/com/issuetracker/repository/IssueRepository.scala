package com.issuetracker.repository

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import com.issuetracker.model.Issue
import com.issuetracker.repository.table.{IssueTable, UserTable}
import scala.concurrent.ExecutionContext.Implicits.global

class IssueRepository(db: Database) {

  lazy val issues = IssueTable.issues
  lazy val users  = UserTable.users

  def create(): Future[Unit] = db.run(issues.schema.create)

  def insert(issue: Issue): Future[Issue] = db.run((issues returning issues) += issue)

  def update(issue: Issue): Future[Option[Issue]] = {
    db.run({
      issues.filter(_.id === issue.id).update(issue).map {
        case 0 => None
        case _ => Some(issue)
      }
    })
  }

  def findByOwnerId(id: Long): Future[Seq[Issue]] = {
    db.run(issues.filter(_.ownerId === id).result)
  }

  def findById(id: Long): Future[Option[Issue]] =
    db.run(issues.filter(_.id === id).result.headOption)

  def findByRepositoryId(repoId: Long): Future[Seq[Issue]] = {
    db.run(issues.filter(_.repositoryId === repoId).result)
  }

}

object IssueRepository {

  def apply(db: Database): IssueRepository =
    new IssueRepository(db)

}
