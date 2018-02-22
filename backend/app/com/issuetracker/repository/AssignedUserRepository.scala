package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.{AssignedUser, Issue, User}
import com.issuetracker.repository.table.{AssignedUserTable, IssueTable, UserTable}
import slick.jdbc.PostgresProfile.api._

class AssignedUserRepository(db: Database) {

  lazy val assignees = AssignedUserTable.assignees
  lazy val users     = UserTable.users
  lazy val issues    = IssueTable.issues

  def create(): Future[Unit] = db.run(assignees.schema.create)

  def drop(): Future[Unit] = db.run(assignees.schema.drop)

  def insert(assignedUser: AssignedUser): Future[AssignedUser] =
    db.run((assignees returning assignees) += assignedUser)

  def findIssueByAssignedUserId(id: Long): Future[Seq[Issue]] =
    db.run({
      for {
        (issue, _) <- issues join assignees.filter(_.userId === id) on (_.id === _.issueId)
      } yield issue
    }.result)

  def insertAssignees(issueId: Long, userIds: Seq[Long]): Future[Option[Int]] =
    db.run({
      assignees ++= userIds.map(AssignedUser(-1, _, issueId))
    })

  def insertAssignee(issueId: Long, userId: Long): Future[AssignedUser] =
    db.run((assignees returning assignees) += AssignedUser(-1, userId, issueId))

  def removeAssignee(issueId: Long, userId: Long): Future[Int] =
    db.run(assignees.filter(_.issueId === issueId).filter(_.userId === userId).delete)

  def findAssigneesByIssueId(issueId: Long): Future[Seq[User]] =
    db.run({
      for {
        c    <- assignees.filter(_.issueId === issueId)
        user <- users.filter(_.id === c.userId)
      } yield user
    }.result)

}

object AssignedUserRepository {

  def apply(db: Database): AssignedUserRepository =
    new AssignedUserRepository(db)

}
