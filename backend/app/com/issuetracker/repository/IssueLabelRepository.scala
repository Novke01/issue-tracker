package com.issuetracker.repository

import scala.concurrent.Future
import com.issuetracker.model._
import com.issuetracker.repository.table._
import slick.jdbc.PostgresProfile.api._

class IssueLabelRepository(db: Database) {

  lazy val issueLabels = IssueLabelTable.issueLabels
  lazy val labels      = LabelTable.labels
  lazy val issues      = IssueTable.issues

  def create(): Future[Unit] = db.run(issueLabels.schema.create)

  def drop(): Future[Unit] = db.run(issueLabels.schema.drop)

  def insertIssueLabels(issueId: Long, labelIds: Seq[Long]): Future[Option[Int]] =
    db.run({
      issueLabels ++= labelIds.map(IssueLabel(-1, _, issueId))
    })

  def insertIssueLabel(issueId: Long, labelId: Long): Future[IssueLabel] =
    db.run((issueLabels returning issueLabels) += IssueLabel(-1, labelId, issueId))

  def removeIssueLabel(issueId: Long, labelId: Long): Future[Int] =
    db.run(issueLabels.filter(_.issueId === issueId).filter(_.labelId === labelId).delete)

  def findLabelsByIssueId(issueId: Long): Future[Seq[Label]] =
    db.run({
      for {
        il    <- issueLabels.filter(_.issueId === issueId)
        label <- labels.filter(_.id === il.labelId)
      } yield label
    }.result)

}

object IssueLabelRepository {

  def apply(db: Database): IssueLabelRepository =
    new IssueLabelRepository(db)

}
