package service

import com.issuetracker.dto.{GetWikiPage, PostWikiPage}
import com.issuetracker.model.WikiPage
import com.issuetracker.repository.{RepositoryRepository, WikiPageRepository}
import com.issuetracker.service.WikiPageService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.postgresql.util.{PSQLException, PSQLState}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatest.Matchers._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class WikiPageServiceSpec extends PlaySpec with MockitoSugar {

  "WikiPageService#findByRepositoryId" should {
    "return all wiki pages from repository with given id" in {

      val repoId = 1
      val wikiPage = WikiPage(
        1,
        "wiki page",
        "wiki page content",
        repoId
      )
      val mockWikiPageRepository = mock[WikiPageRepository]
      when(mockWikiPageRepository.findByRepositoryId(any[Int])) thenReturn Future { Seq(wikiPage) }
      val service = WikiPageService(mockWikiPageRepository, mock[RepositoryRepository])
      service.findByRepositoryId(repoId) map { wikiPages =>
        wikiPages.length mustBe 1
        val returnedWikiPage = wikiPages.head
        returnedWikiPage.id mustBe wikiPage.id
        returnedWikiPage.name mustBe wikiPage.name
        returnedWikiPage.content mustBe wikiPage.content
        returnedWikiPage.repositoryId mustBe wikiPage.repositoryId
      }
    }
    "return empty sequence if the repository with given id doesn't have any wiki page" in {

      val repoId = 1

      val mockWikiPageRepository = mock[WikiPageRepository]
      when(mockWikiPageRepository.findByRepositoryId(any[Int])) thenReturn Future { Seq() }
      val service = WikiPageService(mockWikiPageRepository, mock[RepositoryRepository])
      service.findByRepositoryId(repoId) map { repositories =>
        repositories.length mustBe 0
      }
    }
  }

  "WikiPageService#get" should {
    "return wiki page with given id" in {

      val wikiId = 1
      val wikiPage = WikiPage(
        wikiId,
        "wiki page",
        "wiki page content",
        1
      )
      val mockWikiPageRepository = mock[WikiPageRepository]
      when(mockWikiPageRepository.get(any[Int])) thenReturn Future { Some(wikiPage) }
      val service = WikiPageService(mockWikiPageRepository, mock[RepositoryRepository])
      service.get(wikiId) map { returnedWikiPage =>
        returnedWikiPage.get.id mustBe wikiPage.id
        returnedWikiPage.get.name mustBe wikiPage.name
        returnedWikiPage.get.content mustBe wikiPage.content
        returnedWikiPage.get.repositoryId mustBe wikiPage.repositoryId
      }
    }
    "return None if the wiki page with given id doesn't exist" in {

      val wikiId = 1

      val mockWikiPageRepository = mock[WikiPageRepository]
      when(mockWikiPageRepository.get(any[Int])) thenReturn Future { None }
      val service = WikiPageService(mockWikiPageRepository, mock[RepositoryRepository])
      service.get(wikiId) map { wikiPage =>
        wikiPage mustBe None
      }
    }
  }

  "WikiPageService#insert" should {
    "return new wiki page with valid data" in {

      val wikiPage = PostWikiPage(
        None,
        "wiki page",
        "wiki page content",
        1
      )
      val createdWikiPage = WikiPage(
        1,
        wikiPage.name,
        wikiPage.content,
        wikiPage.repositoryId
      )
      val mockWikiPageRepository = mock[WikiPageRepository]
      when(mockWikiPageRepository.insert(any[WikiPage])) thenReturn Future { createdWikiPage }
      val service = WikiPageService(mockWikiPageRepository, mock[RepositoryRepository])
      service.insert(wikiPage) map { returnedWikiPage =>
        returnedWikiPage.name mustBe wikiPage.name
        returnedWikiPage.content mustBe wikiPage.content
        returnedWikiPage.repositoryId mustBe wikiPage.repositoryId
      }
    }
    "throw PSQLException when repository with given id doesn't exist" in {

      val wikiPage = PostWikiPage(
        None,
        "wiki page",
        "wiki page content",
        1
      )
      val mockWikiPageRepository = mock[WikiPageRepository]
      when(mockWikiPageRepository.insert(any[WikiPage])) thenReturn Future {
        throw new PSQLException("Foreign key constraint violated.", PSQLState.DATA_ERROR)
      }
      val service = WikiPageService(mockWikiPageRepository, mock[RepositoryRepository])
      ScalaFutures.whenReady(service.insert(wikiPage).failed) { e =>
        e shouldBe an[PSQLException]
      }
    }
  }

}
