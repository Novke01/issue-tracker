package com.issuetracker.service

import com.issuetracker.dto.RegisteredUser
import com.issuetracker.model.AssignedUser
import com.issuetracker.repository.{AssignedUserRepository, IssueRepository}

import scala.concurrent.{ExecutionContext, Future}

class AssignedUserService(val assignedUserRepository: AssignedUserRepository,
                          val issueRepository: IssueRepository
                        )(implicit executionContext: ExecutionContext) {

  def findAssigneesByIssueId(id: Long): Future[Seq[RegisteredUser]] = {
    assignedUserRepository.findAssigneesByIssueId(id).map(_.map(RegisteredUser.userToRegisteredUser))
  }

  def insertAssignee(issueId: Long, assignedUserId: Long): Future[AssignedUser] =
    assignedUserRepository.insertAssignee(issueId, assignedUserId)

  def removeAssignee(issueId: Long, assignedUserId: Long): Future[Int] =
    assignedUserRepository.removeAssignee(issueId, assignedUserId)

}

object AssignedUserService {

  def apply(
    assignedUserRepository: AssignedUserRepository,
    issueRepository: IssueRepository
           )(implicit ec: ExecutionContext): AssignedUserService =
    new AssignedUserService(assignedUserRepository, issueRepository)

}