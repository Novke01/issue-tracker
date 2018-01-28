package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.{AssignedUser, Issue, User}
import com.issuetracker.repository.table.{AssignedUserTable, IssueTable, UserTable}
import slick.jdbc.PostgresProfile.api._

class AssignedUserRepository(db: Database) {

  lazy val assignees = AssignedUserTable.assignees
  lazy val users = UserTable.users
  lazy val issues = IssueTable.issues

  def create(): Future[Unit] = db.run(assignees.schema.create)

  def insert(assignedUser: AssignedUser): Future[AssignedUser] = db.run((assignees returning assignees) += assignedUser)

  def findIssueByAssignedUserId(id: Long): Future[Seq[Issue]] = db.run({
    for {
      (issue, _) <- issues join assignees.filter(_.userId === id) on (_.id === _.issueId)
    } yield (issue)
  }.result
  )

  def insertAssignees(issueId: Long, assignedUserIds: Seq[Long]) = db.run({
    assignees ++= assignedUserIds.map(AssignedUser(-1, _, issueId))
  })

  def insertAssignee(issueId: Long, assignedUserId: Long): Future[AssignedUser] =
    db.run((assignees returning assignees) += AssignedUser(-1, assignedUserId, issueId))

  def removeAssignee(issueId: Long, assignedUserId: Long): Future[Int] =
    db.run(assignees.filter(_.issueId === issueId).filter(_.userId === assignedUserId).delete)

  def findAssigneesByIssueId(issueId: Long): Future[Seq[User]] = db.run({
    for {
      c <- assignees.filter(_.issueId === issueId)
      user <- users.filter(_.id === c.userId)
    } yield (user)
  }.result)

}

object AssignedUserRepository {

  def apply(db: Database): AssignedUserRepository =
    new AssignedUserRepository(db)

}