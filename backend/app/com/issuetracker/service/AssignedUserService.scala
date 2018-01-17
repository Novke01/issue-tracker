package com.issuetracker.service

import com.issuetracker.repository.{AssignedUserRepository, IssueRepository}

import scala.concurrent.ExecutionContext

class AssignedUserService(val assignedUserRepository: AssignedUserRepository,
                          val issueRepository: IssueRepository
                        )(implicit executionContext: ExecutionContext) {

}

object AssignedUserService {

  def apply(
    assignedUserRepository: AssignedUserRepository,
    issueRepository: IssueRepository
           )(implicit ec: ExecutionContext): AssignedUserService =
    new AssignedUserService(assignedUserRepository, issueRepository)

}