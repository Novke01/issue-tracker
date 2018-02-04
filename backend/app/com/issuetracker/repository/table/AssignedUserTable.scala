package com.issuetracker.repository.table

import com.issuetracker.model.AssignedUser
import slick.jdbc.PostgresProfile.api._

class AssignedUserTable(tag: Tag) extends Table[AssignedUser](tag, "assignees") {

  def id      = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId  = column[Long]("userId")
  def issueId = column[Long]("issueId")
  def user    = foreignKey("user_FK", userId, UserTable.users)(_.id)
  def issue   = foreignKey("issue_FK", issueId, IssueTable.issues)(_.id)

  def * = (id, userId, issueId) <> (AssignedUser.tupled, AssignedUser.unapply)

}

object AssignedUserTable {

  lazy val assignees = TableQuery[AssignedUserTable]

}
