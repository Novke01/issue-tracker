package com.issuetracker.repository

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import com.issuetracker.model.Label
import com.issuetracker.repository.table.{LabelTable, RepositoryTable}

class LabelRepository(db: Database) {

  lazy val labels       = LabelTable.labels
  lazy val repositories = RepositoryTable.repositories

  def create(): Future[Unit] = db.run(labels.schema.create)

  def insert(label: Label): Future[Label] = db.run((labels returning labels) += label)

  def findById(id: Long): Future[Option[Label]] =
    db.run(labels.filter(_.id === id).result.headOption)

  def findByRepositoryId(repoId: Long): Future[Seq[Label]] = {
    db.run(labels.filter(_.repositoryId === repoId).result)
  }

  def remove(labelId: Long): Future[Int] =
    db.run(labels.filter(_.id === labelId).delete)

}

object LabelRepository {

  def apply(db: Database): LabelRepository =
    new LabelRepository(db)

}
