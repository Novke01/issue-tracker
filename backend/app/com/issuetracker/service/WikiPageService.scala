package com.issuetracker.service

import com.issuetracker.dto.{GetWikiPage, PostWikiPage}

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.issuetracker.repository.WikiPageRepository

class WikiPageService(
    val wikiPageRepository: WikiPageRepository
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

  def delete(id: Long): Future[Int] = {
    wikiPageRepository.delete(id)
  }
}

object WikiPageService {

  def apply(wikiPageRepository: WikiPageRepository)(
      implicit ec: ExecutionContext): WikiPageService =
    new WikiPageService(wikiPageRepository)

}
