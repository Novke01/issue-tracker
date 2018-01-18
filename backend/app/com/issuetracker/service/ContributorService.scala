package com.issuetracker.service

import com.issuetracker.dto.GetUser

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.issuetracker.repository.ContributorRepository
import com.issuetracker.repository.RepositoryRepository
import dto.GetRepository
import dto.PostRepository

class ContributorService(
  val contributorRepository: ContributorRepository,
  val repositoryRepository: RepositoryRepository,
)(implicit val executionContext: ExecutionContext) {

  def insert(postRepository: PostRepository): Future[GetRepository] = {
    repositoryRepository.insert(postRepository) flatMap { repository =>
      contributorRepository.addContributors(repository.id, postRepository.contributors) map { _ =>
        repository
      }
    }
  }

  def findByRepositoryIdAndSearchTerm(repoId: Long, searchTerm: String): Future[Seq[GetUser]] = {
    contributorRepository.findByRepositoryIdAndSearchTerm(repoId, searchTerm).map(_.map(GetUser.userToGetUser))
  }


}

object ContributorService {
  
  def apply(
    contributorRepository: ContributorRepository,
    repositoryRepository: RepositoryRepository
  )(implicit ec: ExecutionContext): ContributorService =
    new ContributorService(contributorRepository, repositoryRepository)
  
}