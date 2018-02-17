package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.PullRequest

case class PostPullRequest(
    title: String,
    url: String,
    repositoryId: Long
)

object PostPullRequest {

  implicit val postPRReads = Json.reads[PostPullRequest]

  implicit def GetPullRequestToPullRequest(pullRequest: PostPullRequest): PullRequest =
    new PullRequest(
      -1,
      pullRequest.title,
      pullRequest.url,
      pullRequest.repositoryId
    )
}
