package com.issuetracker.service

import com.issuetracker.dto.{GetWikiPage, PostWikiPage}
import com.issuetracker.model.WikiPage

import scala.concurrent.{Await, ExecutionContext, Future}
import com.issuetracker.repository.{RepositoryRepository, WikiPageRepository}

import scala.concurrent.duration.Duration

class WikiPageService(
                       val wikiPageRepository: WikiPageRepository,
                       val repositoryRepository: RepositoryRepository
                     )(implicit val ec: ExecutionContext) {

  def findByRepositoryId(id: Long): Future[Seq[GetWikiPage]] = {
    wikiPageRepository.findByRepositoryId(id) map {
      _.map(GetWikiPage.wikiPageToGetWikiPage)
    }
  }

  def get(id: Long): Future[Option[GetWikiPage]] = {
    wikiPageRepository.get(id).map(_.map(GetWikiPage.wikiPageToGetWikiPage))
  }

  def insert(wikiPage: PostWikiPage): Future[GetWikiPage] = {
    wikiPageRepository.insert(wikiPage).map(GetWikiPage.wikiPageToGetWikiPage)
  }

  /**
    * Updates a wiki page.
    *
    * @param wikiPage      Wiki page to be updated.
    * @param currentUserId User id of the user requesting to perform the update.
    */
  def update(wikiPage: WikiPage, currentUserId: Long): Future[GetWikiPage] = {

    Await.result(wikiPageRepository.get(wikiPage.id), Duration.Inf).getOrElse {
      // Wiki page not found, hence can't update.
      throw new IllegalArgumentException()
    }

    val repositoryRecord =
      Await.result(repositoryRepository.get(wikiPage.repositoryId), Duration.Inf).getOrElse {
        // Repository not found, hence can't update.
        throw new IllegalArgumentException()
      }

    // Check weather the current user is the owner of this repository.
    if (repositoryRecord.ownerId != currentUserId) {
      throw new IllegalArgumentException()
    }

    wikiPageRepository.update(wikiPage).map(optionalPage => {
      val page = optionalPage.getOrElse {
        throw new IllegalArgumentException()
      }
      page
    })
  }

}

object WikiPageService {

  def apply(wikiPageRepository: WikiPageRepository, repositoryRepository: RepositoryRepository)(
    implicit ec: ExecutionContext): WikiPageService =
    new WikiPageService(wikiPageRepository, repositoryRepository)

}
