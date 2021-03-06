package com.issuetracker.dto

import com.issuetracker.model.Repository
import play.api.libs.json._

case class PostRepository(
    id: Option[Long],
    name: String,
    url: String,
    description: String,
    ownerId: Long,
    contributors: List[Long]
)

object PostRepository {

  implicit val repositoryReads: Reads[PostRepository] = Json.reads[PostRepository]

  implicit def postRepositoryToRepository(postRepository: PostRepository): Repository =
    Repository(
      postRepository.id.getOrElse(-1),
      postRepository.name,
      postRepository.url,
      postRepository.description,
      postRepository.ownerId
    )

}
