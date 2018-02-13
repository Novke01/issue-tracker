package com.issuetracker.service

import com.issuetracker.dto.RegisteredUser
import com.issuetracker.dto.GetUser

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.issuetracker.repository.ContributorRepository

class ContributorService(
                          val contributorRepository: ContributorRepository
                        )(implicit val executionContext: ExecutionContext) {

  def addContributors(id: Long, contributors: List[Long]): Future[Option[Int]] = {
    contributorRepository.addContributors(id, contributors)
  }

  /**
    * Updates contributors for a repository. Can only add new contributors, not replace/delete existing.
    *
    * @param id           Repository id.
    * @param contributors New proposed set of contributors.
    */
  def updateContributors(id: Long, contributors: List[Long]): Unit = {
    contributorRepository.getContributorsByRepositoryId(id).map(currentContributors => {
      val newContributors = contributors.filter(contributor => !currentContributors.map(user => user.id).contains(contributor))
      contributorRepository.addContributors(id, newContributors)
    })
  }

  def findByRepositoryIdAndSearchTerm(repoId: Long, searchTerm: String): Future[Seq[GetUser]] = {
    contributorRepository
      .findByRepositoryIdAndSearchTerm(repoId, searchTerm)
      .map(_.map(GetUser.userToGetUser))
  }

  def getContributorsByRepositoryId(id: Long): Future[Seq[RegisteredUser]] = {
    contributorRepository
      .getContributorsByRepositoryId(id)
      .map(_.map(RegisteredUser.userToRegisteredUser))
  }

}

object ContributorService {

  def apply(
             contributorRepository: ContributorRepository
           )(implicit ec: ExecutionContext): ContributorService =
    new ContributorService(contributorRepository)

}
