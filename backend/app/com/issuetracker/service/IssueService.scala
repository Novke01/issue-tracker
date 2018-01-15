package com.issuetracker.service

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.dto.{GetIssue, PostIssue}
import com.issuetracker.repository.{AssignedUserRepository, IssueRepository}

class IssueService(val issueRepository: IssueRepository,
                   val asignedUserRepository: AssignedUserRepository)
                 (implicit val executionContext: ExecutionContext) {

  def findByOwnerId(id: Long): Future[Seq[GetIssue]] = {
    issueRepository.findByOwnerId(id).map(_.map(GetIssue.issueToGetIssue))
  }

  def findByAsignedUserId(id: Long): Future[Seq[GetIssue]] = {
    asignedUserRepository.findIssueByAssignedUserId(id).map(_.map(GetIssue.issueToGetIssue))
  }

}