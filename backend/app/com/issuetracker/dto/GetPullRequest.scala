package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.PullRequest

case class GetPullRequest(
    id: Long,
    title: String,
    url: String,
    repositoryId: Long
)

object GetPullRequest {

  implicit val getPRWrites = Json.writes[GetPullRequest]

  implicit def pullRequestToGetPullRequest(pullRequest: PullRequest): GetPullRequest =
    new GetPullRequest(
      pullRequest.id,
      pullRequest.title,
      pullRequest.url,
      pullRequest.repositoryId
    )
}
