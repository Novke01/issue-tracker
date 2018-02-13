package controller

import scala.concurrent.ExecutionContext.Implicits.global
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import akka.stream.Materializer
import play.api.test.Helpers._
import com.issuetracker.controller.WikiPageController
import com.issuetracker.dto.{GetWikiPage, PostWikiPage}
import com.issuetracker.service.WikiPageService
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
