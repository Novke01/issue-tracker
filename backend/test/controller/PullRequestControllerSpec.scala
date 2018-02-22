package controller

import scala.concurrent.ExecutionContext.Implicits.global
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import akka.stream.Materializer
import play.api.test.Helpers._
import com.issuetracker.controller.PullRequestController
import com.issuetracker.dto.{GetPullRequest, PostPullRequest}
import com.issuetracker.service.PullRequestService
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

class PullRequestControllerSpec extends PlaySpec with MockitoSugar with OneAppPerSuiteWithComponents {

  override def components: BuiltInComponents =
    new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
      lazy val router: Router = Router.empty
    }

  implicit lazy val materializer: Materializer = app.materializer

  "PullRequestController#insert" should {
    "return new pull request  data for valid data" in {

      val postPullRequest = PostPullRequest(
        "title",
        "url",
        1
      )

      val pullRequest = GetPullRequest(
        1,
        postPullRequest.title,
        postPullRequest.url,
        postPullRequest.repositoryId
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "title"        -> postPullRequest.title,
          "url"  -> postPullRequest.url,
          "repositoryId" -> postPullRequest.repositoryId
        )
      )
      val mockPullRequest = mock[PullRequestService]
      when(mockPullRequest.insert(any[PostPullRequest])) thenReturn Future { pullRequest }
      val controller             = new PullRequestController(stubControllerComponents(), mockPullRequest)
      val result: Future[Result] = controller.insert().apply(fakeRequest)
      val jsonBody               = contentAsJson(result)
      val jsonNewPullRequest       = Json.toJson(pullRequest)
      jsonBody mustBe jsonNewPullRequest
    }

    "return Bad Request for failed insert" in {

      val postPullRequest = PostPullRequest(
        "title",
        "url",
        1
      )
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "title"        -> postPullRequest.title,
          "url"  -> postPullRequest.url,
          "repositoryId" -> postPullRequest.repositoryId
        )
      )

      val mockPullRequestService = mock[PullRequestService]
      when(mockPullRequestService.insert(any[PostPullRequest])) thenReturn Future {
        throw new Exception
      }
      val controller = new PullRequestController(stubControllerComponents(), mockPullRequestService)

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }
  }

  "PullRequestController#get" should {
    "return pull request with given id" in {

      val pullRequest = GetPullRequest(
        1,
        "title",
        "url",
        1
      )
      val fakeRequest         = FakeRequest()
      val mockPullRequest = mock[PullRequestService]
      when(mockPullRequest.get(any[Long])) thenReturn Future { Some(pullRequest) }
      val controller = new PullRequestController(stubControllerComponents(), mockPullRequest)

      val result: Future[Result] = controller.get(pullRequest.id).apply(fakeRequest)
      val jsonBody               = contentAsJson(result)
      val jsonMilestone           = Json.toJson(pullRequest)
      jsonBody mustBe jsonMilestone
    }

    "return Not found if pull request with given id doesn't exist" in {

      val pullRequestId = 1

      val fakeRequest         = FakeRequest()
      val mockPullRequest = mock[PullRequestService]
      when(mockPullRequest.get(any[Long])) thenReturn Future { None }
      val controller = new PullRequestController(stubControllerComponents(), mockPullRequest)

      val result: Future[Result] = controller.get(pullRequestId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

  "PullRequestController#delete" should {
    "delete pull request and return OK status" in {

      val prId = 1

      val fakeRequest = FakeRequest()
      val mockPullRequestService = mock[PullRequestService]
      when(mockPullRequestService.delete(any[Long])) thenReturn Future {
        1
      }
      val controller = new PullRequestController(stubControllerComponents(), mockPullRequestService)

      val result: Future[Result] = controller.delete(prId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 200
      }
    }

    "return Not found if pull request with given id doesn't exist" in {

      val prId = 1

      val fakeRequest = FakeRequest()
      val mockPullRequestService = mock[PullRequestService]
      when(mockPullRequestService.delete(any[Long])) thenReturn Future {
        0
      }
      val controller = new PullRequestController(stubControllerComponents(), mockPullRequestService)

      val result: Future[Result] = controller.delete(prId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

}
