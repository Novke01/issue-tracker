package dto

import play.api.libs.json._
import com.issuetracker.model.Repository

case class PostRepository(
    name: String,
    url: String,
    description: String,
    ownerId: Long,
    contributors: List[Long]
)

object PostRepository {

  implicit val repositoryReads = Json.reads[PostRepository]

  implicit def postRepositoryToRepository(postRepository: PostRepository): Repository =
    new Repository(
      -1,
      postRepository.name,
      postRepository.url,
      postRepository.description,
      postRepository.ownerId
    )

}
