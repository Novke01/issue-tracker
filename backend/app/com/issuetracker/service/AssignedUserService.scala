package com.issuetracker.service

import com.issuetracker.dto.{GetIssue, PostIssue}
import com.issuetracker.repository.{AssignedUserRepository, IssueRepository}

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class AssignedUserService(val asignedUserRepository: AssignedUserRepository,
                          val issueRepository: IssueRepository,
                        )(implicit executionContext: ExecutionContext) {

  def insert(postIssue: PostIssue): Future[GetIssue] = {
    issueRepository.insert(postIssue) flatMap { issue =>
      asignedUserRepository.insertAssignees(issue.id, postIssue.assignees) map { _ =>
        issue
      }
    }
  }
}