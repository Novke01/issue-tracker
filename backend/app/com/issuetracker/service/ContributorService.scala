package com.issuetracker.service

import com.issuetracker.repository.{ContributorRepository, RepositoryRepository}
import dto.{GetRepository, PostRepository}

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class ContributorService(val contributorRepository: ContributorRepository,
                         val repositoryRepository: RepositoryRepository,
                       )(implicit executionContext: ExecutionContext) {

    def insert(postRepository: PostRepository): Future[GetRepository] = {
      repositoryRepository.insert(postRepository) flatMap { repository =>
        contributorRepository.insertContributors(repository.id, postRepository.contributors) map { _ =>
          repository
        }
      }
    }
}