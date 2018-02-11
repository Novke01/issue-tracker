package com.issuetracker.service

import scala.concurrent.{ExecutionContext, Future}
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

  def insert(postIssue: PostIssue): Future[GetIssue] = {
    issueRepository.insert(postIssue) flatMap { issue =>
      assignedUserRepository.insertAssignees(issue.id, postIssue.assignees) map { _ =>
        issue
      }
      issueLabelRepository.insertIssueLabels(issue.id, postIssue.labels) map { _ =>
        issue
      }
    }
  }

  def update(updateIssue: UpdateIssue): Future[GetIssue] = {
    issueRepository.update(updateIssue) map {
      case Some(issue: Issue) =>
        GetIssue.issueToGetIssue(issue)
      case None =>
        throw new IllegalArgumentException()
    }
  }

  def findByOwnerId(id: Long): Future[Seq[GetIssue]] = {
    issueRepository.findByOwnerId(id).map(_.map(GetIssue.issueToGetIssue))
  }

  def findByAssignedUserId(id: Long): Future[Seq[GetIssue]] = {
    assignedUserRepository.findIssueByAssignedUserId(id).map(_.map(GetIssue.issueToGetIssue))
  }

  def findById(id: Long): Future[GetIssue] = {
    issueRepository.findById(id).map {
      case Some(issue: Issue) =>
        GetIssue.issueToGetIssue(issue)
      case None =>
        logger.info("Issue not found.")
        throw new IssueNotFoundException(s"Issue with id $id doesn't exist.")
    }
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
