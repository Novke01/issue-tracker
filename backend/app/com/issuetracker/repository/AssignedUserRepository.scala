package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.{AssignedUser, Issue}
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

  def insertAssignees(repoId: Long, assignedUserIds: Seq[Long]) = db.run({
    assignees ++= assignedUserIds.map(AssignedUser(-1, _, repoId))
  })

}

object AssignedUserRepository {

  def apply(db: Database): AssignedUserRepository =
    new AssignedUserRepository(db)

}