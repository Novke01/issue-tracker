package controller

import scala.concurrent.ExecutionContext.Implicits.global
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import akka.stream.Materializer
import play.api.test.Helpers._
import com.issuetracker.controller.{RepositoryController, WikiPageController}
import com.issuetracker.dto.{GetWikiPage, JwtUser, PostWikiPage}
import com.issuetracker.model.{Repository, User, WikiPage}
import com.issuetracker.service._
import com.issuetracker.util.JwtUtil
import dto.{GetRepository, PostRepository}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api.libs.json.Json
import play.api.{BuiltInComponents, BuiltInComponentsFromContext, NoHttpFiltersComponents}
import play.api.routing.Router
import play.api.test.FakeRequest
import org.scalatest.Matchers._
import play.api.mvc.Result

import scala.concurrent.Future

class WikiPageControllerSpec extends PlaySpec with MockitoSugar with OneAppPerSuiteWithComponents {

  override def components: BuiltInComponents =
    new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
      lazy val router: Router = Router.empty
    }

  implicit lazy val materializer: Materializer = app.materializer

  "WikiPageController#insert" should {
    "return new wikiPage data for valid data" in {

      val postWikiPage = PostWikiPage(
        null,
        "wiki page name",
        "wiki page content",
        1
      )

      val wikiPage = GetWikiPage(
        1,
        postWikiPage.name,
        postWikiPage.content,
        postWikiPage.repositoryId
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "name"         -> postWikiPage.name,
          "content"      -> postWikiPage.content,
          "repositoryId" -> postWikiPage.repositoryId
        )
      )
      val mockWikiPageService = mock[WikiPageService]
      when(mockWikiPageService.insert(any[PostWikiPage])) thenReturn Future { wikiPage }
      val controller             = new WikiPageController(stubControllerComponents(), mockWikiPageService)
      val result: Future[Result] = controller.insert().apply(fakeRequest)
      val jsonBody               = contentAsJson(result)
      val jsonNewWikiPage        = Json.toJson(wikiPage)
      jsonBody mustBe jsonNewWikiPage
    }

    "return Bad Request for failed insert" in {

      val postWikiPage = PostWikiPage(
        null,
        "wiki page name",
        "wiki page content",
        1
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "name"         -> postWikiPage.name,
          "content"      -> postWikiPage.content,
          "repositoryId" -> postWikiPage.repositoryId
        )
      )

      val mockWikiPageService = mock[WikiPageService]
      when(mockWikiPageService.insert(any[PostWikiPage])) thenReturn Future {
        throw new Exception
      }
      val controller = new WikiPageController(stubControllerComponents(), mockWikiPageService)

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }
  }

  "WikiPageController#update" should {
    "return updated wiki page data for valid data" in {

      val postWikiPage = PostWikiPage(
        Option(1),
        "Wiki title",
        "Wiki content",
        1
      )

      val wikiPage = GetWikiPage(
        1,
        postWikiPage.name,
        postWikiPage.content,
        postWikiPage.repositoryId
      )

      val jwtUser: JwtUser = User(1, null, null, null, null, null, null)

      val fakeRequest = FakeRequest().withMethod("PATCH").withBody(
        Json.obj(
          "id" -> 1,
          "name" -> postWikiPage.name,
          "content" -> postWikiPage.content,
          "repositoryId" -> postWikiPage.repositoryId
        )
      ).addAttr(JwtUser.Key, jwtUser)

      val mockWikiService = mock[WikiPageService]
      when(mockWikiService.update(any[WikiPage], any[Long])) thenReturn Future {
        wikiPage
      }
      val controller = new WikiPageController(stubControllerComponents(), mockWikiService)

      val result: Future[Result] = controller.update().apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonUpdatedWikiPage = Json.toJson(wikiPage)
      jsonBody mustBe jsonUpdatedWikiPage
    }
  }

  "MilestoneController#get" should {
    "return milestone with given id" in {

      val wikiPage = GetWikiPage(
        1,
        "wiki page name",
        "wiki page content",
        1
      )

      val fakeRequest         = FakeRequest()
      val mockWikiPageService = mock[WikiPageService]
      when(mockWikiPageService.get(any[Long])) thenReturn Future { Some(wikiPage) }
      val controller = new WikiPageController(stubControllerComponents(), mockWikiPageService)

      val result: Future[Result] = controller.get(wikiPage.id).apply(fakeRequest)
      val jsonBody               = contentAsJson(result)
      val jsonWikiPage           = Json.toJson(wikiPage)
      jsonBody mustBe jsonWikiPage
    }

    "return Not found if repository with given id doesn't exist" in {

      val wikiPageId = 1

      val fakeRequest         = FakeRequest()
      val mockWikiPageService = mock[WikiPageService]
      when(mockWikiPageService.get(any[Long])) thenReturn Future { None }
      val controller = new WikiPageController(stubControllerComponents(), mockWikiPageService)

      val result: Future[Result] = controller.get(wikiPageId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

}
