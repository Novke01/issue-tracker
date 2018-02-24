package com.issuetracker.service

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.dto._
import com.issuetracker.model.Label
import com.issuetracker.repository.LabelRepository
import play.api.Logger

class LabelService(val labelRepository: LabelRepository)(
    implicit val executionContext: ExecutionContext) {

  def insert(label: Label): Future[GetLabel] = {
    labelRepository.insert(label).map(GetLabel.labelToGetLabel)
  }

  def findByRepositoryId(repoId: Long): Future[Seq[GetLabel]] = {
    labelRepository.findByRepositoryId(repoId).map(_.map(GetLabel.labelToGetLabel))
  }

  def findById(id: Long): Future[Option[GetLabel]] = {
    labelRepository.findById(id).map(_.map(GetLabel.labelToGetLabel))
  }

  def remove(labelId: Long): Future[Int] =
    labelRepository.remove(labelId)

}

object LabelService {

  def apply(
      labelRepository: LabelRepository
  )(implicit ec: ExecutionContext): LabelService =
    new LabelService(labelRepository)

}
