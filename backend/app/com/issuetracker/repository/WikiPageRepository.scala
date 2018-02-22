package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.{Repository, WikiPage}
import slick.jdbc.PostgresProfile.api._
import com.issuetracker.repository.table.RepositoryTable
import com.issuetracker.repository.table.WikiPageTable
import com.issuetracker.repository.table.UserTable

class WikiPageRepository(db: Database) {

  lazy val wikiPages    = WikiPageTable.wikiPages
  lazy val repositories = RepositoryTable.repositories
  lazy val users        = UserTable.users

  def create(): Future[Unit] = db.run(wikiPages.schema.create)

  def drop(): Future[Unit] = db.run(wikiPages.schema.drop)

  def insert(wikiPage: WikiPage): Future[WikiPage] =
    db.run((wikiPages returning wikiPages) += wikiPage)

  def findByRepositoryId(id: Long): Future[Seq[WikiPage]] =
    db.run(wikiPages.filter(_.repositoryId === id).result)

  def get(id: Long): Future[Option[WikiPage]] =
    db.run(wikiPages.filter(_.id === id).result.headOption)

  def delete(id: Long): Future[Int] =
    db.run(wikiPages.filter(_.id === id).delete)

}

object WikiPageRepository {

  def apply(db: Database): RepositoryRepository =
    new RepositoryRepository(db)

}
