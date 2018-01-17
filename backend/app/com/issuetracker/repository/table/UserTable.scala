package com.issuetracker.repository.table

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.model.User

class UserTable(tag: Tag) extends Table[User](tag, "users") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username", O.Unique)
  def password = column[String]("password")
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def email = column[String]("email", O.Unique)
  def refreshToken = column[String]("refresh_token")

  def * = (id, username, password, firstName, lastName, email, refreshToken) <> (User.tupled, User.unapply)

}

object UserTable {
  
  lazy val users = TableQuery[UserTable]
  
}