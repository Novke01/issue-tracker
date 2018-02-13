package controller

import scala.concurrent.ExecutionContext.Implicits.global
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import akka.stream.Materializer
import play.api.test.Helpers._
import com.issuetracker.controller.MilestoneController
import com.issuetracker.dto.{GetMilestone, PostMilestone}
import com.issuetracker.service.MilestoneService
import org.joda.time.DateTime
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

class MilestoneControllerSpec extends PlaySpec with MockitoSugar with OneAppPerSuiteWithComponents {

  override def components: BuiltInComponents =
    new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
      lazy val router: Router = Router.empty
    }

  implicit lazy val materializer: Materializer = app.materializer

  "MilestoneController#insert" should {
    "return new milestone  data for valid data" in {

      val postMilestone = PostMilestone(
        "title",
        "description",
        new DateTime(),
        1
      )

      val milestone = GetMilestone(
        1,
        postMilestone.title,
        postMilestone.description,
        postMilestone.dueDate,
        postMilestone.repositoryId
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "title"        -> postMilestone.title,
          "description"  -> postMilestone.description,
          "dueDate"      -> postMilestone.dueDate.toString("dd/MM/yyyy"),
          "repositoryId" -> postMilestone.repositoryId
        )
      )
      val mockMilestoneService = mock[MilestoneService]
      when(mockMilestoneService.insert(any[PostMilestone])) thenReturn Future { milestone }
      val controller             = new MilestoneController(stubControllerComponents(), mockMilestoneService)
      val result: Future[Result] = controller.insert().apply(fakeRequest)
      val jsonBody               = contentAsJson(result)
      val jsonNewMilestone       = Json.toJson(milestone)
      jsonBody mustBe jsonNewMilestone
    }

    "return Bad Request for failed insert" in {

      val postMilestone = PostMilestone(
        "title",
        "description",
        new DateTime(),
        1
      )
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "title"        -> postMilestone.title,
          "description"  -> postMilestone.description,
          "dueDate"      -> postMilestone.dueDate.toString("dd/MM/yyyy"),
          "repositoryId" -> postMilestone.repositoryId
        )
      )

      val mockMilestoneService = mock[MilestoneService]
      when(mockMilestoneService.insert(any[PostMilestone])) thenReturn Future {
        throw new Exception
      }
      val controller = new MilestoneController(stubControllerComponents(), mockMilestoneService)

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }
  }

  "MilestoneController#get" should {
    "return milestone with given id" in {

      val milestone = GetMilestone(
        1,
        "title",
        "description",
        new DateTime(),
        1
      )
      val fakeRequest         = FakeRequest()
      val mockMilestoneService = mock[MilestoneService]
      when(mockMilestoneService.get(any[Long])) thenReturn Future { Some(milestone) }
      val controller = new MilestoneController(stubControllerComponents(), mockMilestoneService)

      val result: Future[Result] = controller.get(milestone.id).apply(fakeRequest)
      val jsonBody               = contentAsJson(result)
      val jsonMilestone           = Json.toJson(milestone)
      jsonBody mustBe jsonMilestone
    }

    "return Not found if milestone with given id doesn't exist" in {

      val miletoneId = 1

      val fakeRequest         = FakeRequest()
      val mockMilestoneService = mock[MilestoneService]
      when(mockMilestoneService.get(any[Long])) thenReturn Future { None }
      val controller = new MilestoneController(stubControllerComponents(), mockMilestoneService)

      val result: Future[Result] = controller.get(miletoneId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

}
