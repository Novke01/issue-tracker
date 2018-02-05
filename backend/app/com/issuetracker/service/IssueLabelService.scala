package com.issuetracker.service

import com.issuetracker.dto.GetLabel
import com.issuetracker.model.IssueLabel
import com.issuetracker.repository.{IssueLabelRepository, IssueRepository}

import scala.concurrent.{ExecutionContext, Future}

class IssueLabelService(
    val issueLabelRepository: IssueLabelRepository,
    val issueRepository: IssueRepository)(implicit executionContext: ExecutionContext) {

  def findLabelsByIssueId(id: Long): Future[Seq[GetLabel]] = {
    issueLabelRepository
      .findLabelsByIssueId(id)
      .map(_.map(GetLabel.labelToGetLabel))
  }

  def insertLabel(issueId: Long, labelId: Long): Future[IssueLabel] =
    issueLabelRepository.insertIssueLabel(issueId, labelId)

  def removeLabel(issueId: Long, labelId: Long): Future[Int] =
    issueLabelRepository.removeIssueLabel(issueId, labelId)

}

object IssueLabelService {

  def apply(
      issueLabelRepository: IssueLabelRepository,
      issueRepository: IssueRepository
  )(implicit ec: ExecutionContext): IssueLabelService =
    new IssueLabelService(issueLabelRepository, issueRepository)

}
