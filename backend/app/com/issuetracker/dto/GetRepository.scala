package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.Repository

case class GetRepository(
    id: Long,
    name: String,
    url: String,
    description: String,
    ownerId: Long
)

object GetRepository {

  implicit val repositoryWrites: OWrites[GetRepository] = Json.writes[GetRepository]

  implicit def repositoryToGetRepository(repository: Repository): GetRepository =
    new GetRepository(
      repository.id,
      repository.name,
      repository.url,
      repository.description,
      repository.ownerId
    )

}
