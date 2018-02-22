package controller

import akka.stream.Materializer
import com.issuetracker.dto.{GetIssue, PostIssue}
import com.issuetracker.model.{Issue, IssueStatus}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api.{BuiltInComponents, BuiltInComponentsFromContext, NoHttpFiltersComponents}
import play.api.routing.Router
import java.lang.System.currentTimeMillis

import com.issuetracker.controller.IssueController
import com.issuetracker.service.{AssignedUserService, IssueLabelService, IssueService}
import com.issuetracker.util.JwtUtil
import play.api.libs.json.Json
import play.api.test.FakeRequest
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.test.Helpers._
import play.api.mvc.Result

import scala.concurrent.Future

class IssueControllerSpec extends PlaySpec with MockitoSugar with OneAppPerSuiteWithComponents {

  override def components: BuiltInComponents =
    new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
      lazy val router: Router = Router.empty
    }

  implicit lazy val materializer: Materializer = app.materializer

  "IssueController#insert" should {
    "return new issue data for valid data" in {

      val postIssue = PostIssue(
        1l,
        "newIssueTitle",
        "newIssueDescription",
        1l,
        List[Long](),
        List[Long](),
        Some(1l)
      )

      val issue = GetIssue(
        1l,
        postIssue.repositoryId,
        postIssue.title,
        postIssue.description,
        currentTimeMillis(),
        postIssue.ownerId,
        IssueStatus.OPENED,
        postIssue.milestoneId
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "repositoryId" -> postIssue.repositoryId,
          "title"        -> postIssue.title,
          "description"  -> postIssue.description,
          "ownerId"      -> postIssue.ownerId,
          "assignees"    -> postIssue.assignees,
          "labels"       -> postIssue.labels
        )
      )

      val mockIssueService = mock[IssueService]
      when(mockIssueService.insert(any[Issue], any[List[Long]], any[List[Long]])) thenReturn Future {
        issue
      }
      val controller = new IssueController(stubControllerComponents(),
                                           mockIssueService,
                                           mock[IssueLabelService],
                                           mock[AssignedUserService],
                                           mock[JwtUtil])

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      val jsonBody               = contentAsJson(result)
      val jsonNewIssue           = Json.toJson(issue)
      jsonBody mustBe jsonNewIssue
    }

    "return Bad Request for failed insert" in {

      val postIssue = PostIssue(
        1l,
        "newIssueTitle",
        "newIssueDescription",
        1l,
        List[Long](),
        List[Long](),
        Some(1l)
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "repositoryId" -> postIssue.repositoryId,
          "title"        -> postIssue.title,
          "description"  -> postIssue.description,
          "ownerId"      -> postIssue.ownerId,
          "assignees"    -> postIssue.assignees,
          "labels"       -> postIssue.labels
        )
      )
      val mockIssueService = mock[IssueService]
      when(mockIssueService.insert(any[Issue], any[List[Long]], any[List[Long]])) thenReturn Future {
        throw new Exception
      }
      val controller = new IssueController(stubControllerComponents(),
                                           mockIssueService,
                                           mock[IssueLabelService],
                                           mock[AssignedUserService],
                                           mock[JwtUtil])

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }

    "return Bad Request for invalid data" in {

      val postIssue = PostIssue(
        1l,
        "newIssueTitle",
        "newIssueDescription",
        1l,
        List[Long](),
        List[Long](),
        Some(1l)
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "repositoryId" -> postIssue.repositoryId,
          "title"        -> postIssue.title,
          "description"  -> postIssue.description,
          "ownerId"      -> postIssue.ownerId
          // no assignees and labels
        )
      )

      val controller = new IssueController(stubControllerComponents(),
                                           mock[IssueService],
                                           mock[IssueLabelService],
                                           mock[AssignedUserService],
                                           mock[JwtUtil])

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }
  }

  "IssueController#update" should {
    "return updated issue data for valid data" in {

      val updateIssue = Issue(
        1,
        1,
        "updatedTitle",
        "updatedDescription",
        currentTimeMillis(),
        1,
        IssueStatus.CLOSED,
        Some(1)
      )

      val issue = GetIssue(
        updateIssue.id,
        updateIssue.repositoryId,
        updateIssue.title,
        updateIssue.description,
        updateIssue.created,
        updateIssue.ownerId,
        updateIssue.status,
        updateIssue.milestoneId
      )

      val fakeRequest = FakeRequest()
        .withMethod("PUT")
        .withBody(
          Json.obj(
            "id"           -> 1,
            "repositoryId" -> updateIssue.repositoryId,
            "title"        -> updateIssue.title,
            "description"  -> updateIssue.description,
            "created"      -> updateIssue.created,
            "ownerId"      -> updateIssue.ownerId,
            "status"       -> updateIssue.status
          )
        )

      val mockIssueService = mock[IssueService]
      when(mockIssueService.update(any[Issue])) thenReturn Future {
        issue
      }
      val controller = new IssueController(stubControllerComponents(),
                                           mockIssueService,
                                           mock[IssueLabelService],
                                           mock[AssignedUserService],
                                           mock[JwtUtil])

      val result: Future[Result] = controller.update().apply(fakeRequest)
      val jsonBody               = contentAsJson(result)
      val jsonUpdatedIssue       = Json.toJson(issue)
      jsonBody mustBe jsonUpdatedIssue
    }

    "return Bad Request for failed update" in {

      val updateIssue = Issue(
        1,
        1,
        "updatedTitle",
        "updatedDescription",
        currentTimeMillis(),
        1,
        IssueStatus.CLOSED,
        Some(1)
      )

      val fakeRequest = FakeRequest()
        .withMethod("PUT")
        .withBody(
          Json.obj(
            "id"           -> 1,
            "repositoryId" -> updateIssue.repositoryId,
            "title"        -> updateIssue.title,
            "description"  -> updateIssue.description,
            "created"      -> updateIssue.created,
            "ownerId"      -> updateIssue.ownerId,
            "status"       -> updateIssue.status
          )
        )
      val mockIssueService = mock[IssueService]
      when(mockIssueService.update(any[Issue])) thenReturn Future {
        throw new Exception
      }
      val controller = new IssueController(stubControllerComponents(),
        mockIssueService,
        mock[IssueLabelService],
        mock[AssignedUserService],
        mock[JwtUtil])

      val result: Future[Result] = controller.update().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }

    "return Bad Request for invalid data" in {

      val updateIssue = Issue(
        1,
        1,
        "updatedTitle",
        "updatedDescription",
        currentTimeMillis(),
        1,
        IssueStatus.CLOSED,
        Some(1)
      )

      val fakeRequest = FakeRequest()
        .withMethod("PUT")
        .withBody(
          Json.obj(
            "id"           -> 1,
            "repositoryId" -> updateIssue.repositoryId,
            "title"        -> updateIssue.title,
            "description"  -> updateIssue.description,
            "created"      -> updateIssue.created,
            "ownerId"      -> "wrongType",
            "status"       -> updateIssue.status
          )
        )

      val controller = new IssueController(stubControllerComponents(),
        mock[IssueService],
        mock[IssueLabelService],
        mock[AssignedUserService],
        mock[JwtUtil])

      val result: Future[Result] = controller.update().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }
  }

  "IssueController#getOne" should {
    "return issue with given id" in {

      val issue = GetIssue(
        1l,
        1l,
        "newIssueTitle",
        "newIssueDescription",
        currentTimeMillis(),
        1l,
        IssueStatus.OPENED,
        Some(1)
      )

      val fakeRequest = FakeRequest()
      val mockIssueService = mock[IssueService]
      when(mockIssueService.findById(any[Long])) thenReturn Future {
        Some(issue)
      }
      val controller = new IssueController(stubControllerComponents(),
        mockIssueService,
        mock[IssueLabelService],
        mock[AssignedUserService],
        mock[JwtUtil])

      val result: Future[Result] = controller.getOne(issue.id).apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonIssue = Json.toJson(issue)
      jsonBody mustBe jsonIssue
    }

    "return Not found if issue with given id doesn't exist" in {

      val issueId = 1

      val fakeRequest = FakeRequest()
      val mockIssueService = mock[IssueService]
      when(mockIssueService.findById(any[Long])) thenReturn Future {
        None
      }
      val controller = new IssueController(stubControllerComponents(),
        mockIssueService,
        mock[IssueLabelService],
        mock[AssignedUserService],
        mock[JwtUtil])

      val result: Future[Result] = controller.getOne(issueId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

  "IssueController#delete" should {
    "return delete issue and return OK status" in {

      val issueId = 1;

      val fakeRequest = FakeRequest()
      val mockIssueService = mock[IssueService]
      when(mockIssueService.delete(any[Long])) thenReturn Future {
        1
      }
      val controller = new IssueController(stubControllerComponents(),
        mockIssueService,
        mock[IssueLabelService],
        mock[AssignedUserService],
        mock[JwtUtil])

      val result: Future[Result] = controller.delete(issueId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 200
      }
    }

    "return Not found if issue with given id doesn't exist" in {

      val issueId = 1;

      val fakeRequest = FakeRequest()
      val mockIssueService = mock[IssueService]
      when(mockIssueService.delete(any[Long])) thenReturn Future {
        0
      }
      val controller = new IssueController(stubControllerComponents(),
        mockIssueService,
        mock[IssueLabelService],
        mock[AssignedUserService],
        mock[JwtUtil])

      val result: Future[Result] = controller.delete(issueId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

}
