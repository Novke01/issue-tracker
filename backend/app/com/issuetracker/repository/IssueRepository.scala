package com.issuetracker.repository

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import com.issuetracker.model.Issue
import com.issuetracker.model.IssueStatus._

class IssueRepository(db: Database, val userRepository: UserRepository) {

  lazy val issues = TableQuery[IssueTable]
  lazy val users = userRepository.users

  private[IssueRepository] class IssueTable(tag: Tag) extends Table[Issue](tag, "issues") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def description = column[String]("description")
    def created = column[Long]("created")
    def status = column[IssueStatus]("status")
    def ownerId = column[Long]("ownerId")
    def owner = foreignKey("owner_FK", ownerId, users)(_.id)

    def * = (id, title, description, created, ownerId, status) <> (Issue.tupled, Issue.unapply)

  }

  def create(): Future[Unit] = db.run(issues.schema.create)

  def insert(issue: Issue): Future[Issue] = db.run((issues returning issues) += issue)

  def findByOwnerId(id: Long): Future[Seq[Issue]] = {
    db.run(issues.filter(_.ownerId === id).result)
  }

  def findById(id: Long): Future[Option[Issue]] =
    db.run(issues.filter(_.id === id).result.headOption)

}