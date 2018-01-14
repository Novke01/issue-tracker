package com.issuetracker.repository

import scala.concurrent.Future

import com.issuetracker.model.User

import slick.jdbc.PostgresProfile.api.Database
import slick.jdbc.PostgresProfile.api.Table
import slick.jdbc.PostgresProfile.api.TableQuery
import slick.jdbc.PostgresProfile.api.Tag
import slick.jdbc.PostgresProfile.api.anyToShapedValue
import slick.jdbc.PostgresProfile.api.columnExtensionMethods
import slick.jdbc.PostgresProfile.api.longColumnType
import slick.jdbc.PostgresProfile.api.queryInsertActionExtensionMethods
import slick.jdbc.PostgresProfile.api.schemaActionExtensionMethods
import slick.jdbc.PostgresProfile.api.streamableQueryActionExtensionMethods
import slick.jdbc.PostgresProfile.api.stringColumnType
import slick.jdbc.PostgresProfile.api.tableQueryToTableQueryExtensionMethods
import slick.jdbc.PostgresProfile.api.valueToConstColumn

class UserRepository(db: Database) {
  
  lazy val users = TableQuery[UserTable]

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
  
  def create(): Future[Unit] = db.run(users.schema.create)
  
  def insert(user: User): Future[User] = db.run((users returning users) += user)
  
  def findByUsername(username: String): Future[Option[User]] =
    db.run(users.filter(_.username === username).result.headOption)
 
  def findById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)
    
}