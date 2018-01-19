package com.issuetracker.service

import com.issuetracker.dto.RegisteredUser

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.issuetracker.repository.RepositoryRepository
import dto.GetRepository

class RepositoryService(
  val repositoryRepository: RepositoryRepository
)(implicit val ec: ExecutionContext) {

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
  
  def apply(repositoryRepository: RepositoryRepository)
    (implicit ec: ExecutionContext): RepositoryService = 
    new RepositoryService(repositoryRepository)
  
}