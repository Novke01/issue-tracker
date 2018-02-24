package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model.Milestone
import slick.jdbc.PostgresProfile.api._
import com.issuetracker.repository.table.MilestoneTable

class MilestoneRepository(db: Database) {

  lazy val milestones = MilestoneTable.milestones

  def create(): Future[Unit] = db.run(milestones.schema.create)

  def drop(): Future[Unit] = db.run(milestones.schema.drop)

  def insert(milestone: Milestone): Future[Milestone] =
    db.run((milestones returning milestones) += milestone)

  def get(id: Long): Future[Option[Milestone]] =
    db.run(milestones.filter(_.id === id).result.headOption)

  def delete(id: Long): Future[Int] =
    db.run(milestones.filter(_.id === id).delete)

  def getByRepositoryId(id: Long): Future[Seq[Milestone]] =
    db.run(milestones.filter(_.repositoryId === id).result)
}

object MilestoneRepository {

  def apply(db: Database): MilestoneRepository =
    new MilestoneRepository(db)

}
