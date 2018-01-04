package com.issuetracker.repository

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import com.issuetracker.model.User

class UserRepository(db: Database) {
  
  lazy val Users = TableQuery[UserTable]

  private[UserRepository] class UserTable(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username", O.Unique)
    def password = column[String]("password")
    def firstName = column[String]("first_name")
    def lastName = column[String]("last_name")
    def email = column[String]("email", O.Unique)

    def * = (id, username, password, firstName, lastName, email) <> (User.tupled, User.unapply)

  }

  def all(): Future[Seq[User]] = db.run(Users.result)
  
  def insert(user: User): Future[User] = db.run((Users returning Users) += user)
  
  def create(): Future[Unit] = db.run(Users.schema.create)
  
}