package com.issuetracker.repository

import scala.concurrent.Future

import com.issuetracker.model.User

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.repository.table.UserTable

class UserRepository(db: Database) {

  lazy val users = UserTable.users

  def create(): Future[Unit] = db.run(users.schema.create)

  def insert(user: User): Future[User] = db.run((users returning users) += user)

  def findByUsername(username: String): Future[Option[User]] =
    db.run(users.filter(_.username === username).result.headOption)

  def findById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  def all(): Future[Seq[User]] = db.run(users.result)

}

object UserRepository {

  def apply(db: Database): UserRepository =
    new UserRepository(db)

}
