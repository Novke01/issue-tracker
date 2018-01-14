package com.issuetracker.service

import com.issuetracker.repository.{ContributorRepository, RepositoryRepository}
import dto.{GetRepository}

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class RepositoryService(
                   val repositoryRepository: RepositoryRepository,
                   val contributorRepository: ContributorRepository
                 )(implicit executionContext: ExecutionContext) {

  def findByOwnerId(id: Long): Future[Seq[GetRepository]] = {
    repositoryRepository.findByOwnerId(id).map(_.map(GetRepository.repositoryToGetRepository))
  }

  def findByContributorId(id: Long): Future[Seq[GetRepository]] = {
    contributorRepository.findRepositoryByContributorId(id).map(_.map(GetRepository.repositoryToGetRepository))
  }

}