package com.issuetracker.repository

import scala.concurrent.Future

import com.issuetracker.model.User

import slick.jdbc.PostgresProfile.api._

class UserRepository(db: Database) {
  
  lazy val users = TableQuery[UserTable]

  private[UserRepository] class UserTable(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username", O.Unique)
    def password = column[String]("password")
    def firstName = column[String]("first_name")
    def lastName = column[String]("last_name")
    def email = column[String]("email", O.Unique)
    def refreshToken = column[String]("refresh_token")

    def * = (id, username, password, firstName, lastName, email, refreshToken) <> (User.tupled, User.unapply)

  }
  
  def create(): Future[Unit] = db.run(users.schema.create)
  
  def insert(user: User): Future[User] = db.run((users returning users) += user)
  
  def findByUsername(username: String): Future[Option[User]] =
    db.run(users.filter(_.username === username).result.headOption)
 
  def findById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)
    
}