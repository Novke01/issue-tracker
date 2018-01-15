package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.{AssignedUser, Issue}
import slick.jdbc.PostgresProfile.api._

class AssignedUserRepository(db: Database, val userRepository: UserRepository, val issueRepository: IssueRepository) {

  lazy val assignees = TableQuery[AssignedUserTable]
  lazy val users = userRepository.users
  lazy val issues = issueRepository.issues

  class AssignedUserTable(tag: Tag) extends Table[AssignedUser](tag, "assignees") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userId = column[Long]("userId")

    def issueId = column[Long]("issueId")

    def user = foreignKey("user_FK", userId, users)(_.id)

    def issue = foreignKey("issue_FK", issueId, issues)(_.id)

    def * = (id, userId, issueId) <> (AssignedUser.tupled, AssignedUser.unapply)

  }

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