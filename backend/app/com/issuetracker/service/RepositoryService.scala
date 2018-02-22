package com.issuetracker.service

import com.issuetracker.dto.{GetRepository, PostRepository, RegisteredUser}
import com.issuetracker.model.Repository
import com.issuetracker.repository.RepositoryRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class RepositoryService(
    val repositoryRepository: RepositoryRepository,
    val contributorService: ContributorService
)(implicit val ec: ExecutionContext) {

  def insert(postRepository: PostRepository): Future[GetRepository] = {
    repositoryRepository.insert(postRepository) map { repository =>
      contributorService.addContributors(repository.id, postRepository.contributors)
      repository
    }
  }

  /**
    * Updates a repository, basic information and contributors.
    *
    * @param repository    Repository to be updated.
    * @param contributors  Contributor id list.
    * @param currentUserId User id of the user requesting to perform the update.
    */
  def update(repository: Repository,
             contributors: List[Long],
             currentUserId: Long): Future[GetRepository] = {

    val repositoryRecord =
      Await.result(repositoryRepository.get(repository.id), Duration.Inf).getOrElse {
        // Repository not found, hence can't update.
        throw new IllegalArgumentException()
      }

    // Check weather the current user is the owner of this repository.
    if (repositoryRecord.ownerId != currentUserId) {
      throw new IllegalArgumentException()
    }

    repositoryRepository.update(repository) map { repositoryOptional =>
      val repository = repositoryOptional.getOrElse {
        // Repository was deleted in the meanwhile.
        throw new IllegalArgumentException()
      }

      // An owner is not counted as a contributor, hence, remove him from the candidates if he is in there.
      val contributorsWithoutOwner = contributors.filter(id => id != repositoryRecord.ownerId)
      contributorService.updateContributors(repository.id, contributorsWithoutOwner)
      repository
    }
  }

  def findByOwnerId(id: Long): Future[Seq[GetRepository]] = {
    repositoryRepository.findByOwnerId(id) map {
      _.map(GetRepository.repositoryToGetRepository)
    }
  }

  def findByContributorId(id: Long): Future[Seq[GetRepository]] = {
    repositoryRepository.findByContributorId(id) map {
      _.map(GetRepository.repositoryToGetRepository)
    }
  }

  def get(id: Long): Future[Option[GetRepository]] = {
    repositoryRepository.get(id).map(_.map(GetRepository.repositoryToGetRepository))
  }

  def getRepositoryOwner(id: Long): Future[Option[RegisteredUser]] = {
    repositoryRepository.getRepositoryOwner(id).map(_.map(RegisteredUser.userToRegisteredUser))
  }

}

object RepositoryService {

  def apply(repositoryRepository: RepositoryRepository, contributorService: ContributorService)(
      implicit ec: ExecutionContext): RepositoryService =
    new RepositoryService(repositoryRepository, contributorService)

}
