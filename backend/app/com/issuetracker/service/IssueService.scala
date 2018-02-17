package com.issuetracker.service

import scala.concurrent.{Await, ExecutionContext, Future}
import com.issuetracker.dto.{GetIssue, PostIssue, UpdateIssue}
import com.issuetracker.exception.IssueNotFoundException
import com.issuetracker.model.Issue
import com.issuetracker.repository.{AssignedUserRepository, IssueLabelRepository, IssueRepository}
import play.api.Logger

class IssueService(val issueRepository: IssueRepository,
                   val issueLabelRepository: IssueLabelRepository,
                   val assignedUserRepository: AssignedUserRepository)(
    implicit val executionContext: ExecutionContext) {

  private val logger = Logger(this.getClass())

  def insert(postIssue: Issue, assignees: List[Long], labels: List[Long]): Future[GetIssue] = {
    issueRepository.insert(postIssue) flatMap { issue =>
      assignedUserRepository.insertAssignees(issue.id, assignees) map { _ =>
        issue
      }
      issueLabelRepository.insertIssueLabels(issue.id, labels) map { _ =>
        issue
      }
    }
  }

  def update(updateIssue: Issue): Future[GetIssue] = {
    issueRepository.update(updateIssue) map {
      case Some(issue: Issue) =>
        GetIssue.issueToGetIssue(issue)
      case None =>
        throw new IllegalArgumentException()
    }
  }

  def findById(id: Long): Future[Option[GetIssue]] = {
    issueRepository.findById(id).map(_.map(GetIssue.issueToGetIssue))
  }

  def findByRepositoryId(repoId: Long): Future[Seq[GetIssue]] = {
    issueRepository.findByRepositoryId(repoId).map(_.map(GetIssue.issueToGetIssue))
  }

}

object IssueService {

  def apply(
      issueRepository: IssueRepository,
      issueLabelRepository: IssueLabelRepository,
      assignedUserRepository: AssignedUserRepository
  )(implicit ec: ExecutionContext): IssueService =
    new IssueService(issueRepository, issueLabelRepository, assignedUserRepository)

}
