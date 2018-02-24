package com.issuetracker.service

import com.issuetracker.dto.{GetPullRequest, PostPullRequest}

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.issuetracker.repository.PullRequestRepository

class PullRequestService(
    val pullRequestRepository: PullRequestRepository
)(implicit val ec: ExecutionContext) {

  def insert(pullRequest: PostPullRequest): Future[GetPullRequest] = {
    pullRequestRepository.insert(pullRequest).map(GetPullRequest.pullRequestToGetPullRequest)
  }

  def get(id: Long): Future[Option[GetPullRequest]] = {
    pullRequestRepository.get(id).map(_.map(GetPullRequest.pullRequestToGetPullRequest))
  }

  def getByRepositoryId(id: Long): Future[Seq[GetPullRequest]] = {
    pullRequestRepository
      .getByRepositoryId(id)
      .map(_.map(GetPullRequest.pullRequestToGetPullRequest))
  }

  def delete(id: Long): Future[Int] = {
    pullRequestRepository.delete(id)
  }
}

object PullRequestService {

  def apply(pullRequestRepository: PullRequestRepository)(
      implicit ec: ExecutionContext): PullRequestService =
    new PullRequestService(pullRequestRepository)

}
