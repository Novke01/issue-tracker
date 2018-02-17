package controller

import akka.stream.Materializer
import com.issuetracker.controller.LabelController
import com.issuetracker.dto.{GetLabel, PostLabel}
import com.issuetracker.model.Label
import com.issuetracker.service.LabelService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api.libs.json.Json
import play.api.{BuiltInComponents, BuiltInComponentsFromContext, NoHttpFiltersComponents}
import play.api.routing.Router
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, stubControllerComponents}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.test.Helpers._
import play.api.mvc.Result

import scala.concurrent.Future

class LabelControllerSpec extends PlaySpec with MockitoSugar with OneAppPerSuiteWithComponents {

  override def components: BuiltInComponents =
    new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
      lazy val router: Router = Router.empty
    }

  implicit lazy val materializer: Materializer = app.materializer

  "LabelController#insert" should {
    "return new label data for valid data" in {

      val postLabel = PostLabel(
        "newLabel",
        "#ff0000",
        1l
      )

      val label = GetLabel(
        1l,
        postLabel.name,
        postLabel.color,
        postLabel.repositoryId
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "name"         -> postLabel.name,
          "color"        -> postLabel.color,
          "repositoryId" -> postLabel.repositoryId
        )
      )

      val mockLabelService = mock[LabelService]
      when(mockLabelService.insert(any[Label])) thenReturn Future {
        label
      }
      val controller = new LabelController(stubControllerComponents(),
        mockLabelService)

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      val jsonBody               = contentAsJson(result)
      val jsonNewLabel           = Json.toJson(label)
      jsonBody mustBe jsonNewLabel
    }

    "return Bad Request for failed insert" in {

      val postLabel = PostLabel(
        "newLabel",
        "#ff0000",
        1l
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "name"         -> postLabel.name,
          "color"        -> postLabel.color,
          "repositoryId" -> postLabel.repositoryId
        )
      )
      val mockLabelService = mock[LabelService]
      when(mockLabelService.insert(any[Label])) thenReturn Future {
        throw new Exception
      }
      val controller = new LabelController(stubControllerComponents(), mockLabelService)

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }

    "return Bad Request for invalid data" in {

      val postLabel = PostLabel(
        "newLabel",
        "#ff0000",
        1l
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "name"         -> postLabel.name,
          "repositoryId" -> postLabel.repositoryId
          // no color
        )
      )

      val controller = new LabelController(stubControllerComponents(), mock[LabelService])

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }

  }

  "LabelController#getOne" should {
    "return label with given id" in {

      val label = GetLabel(
        1l,
        "name",
        "#ff0000",
        1l
      )

      val fakeRequest = FakeRequest()
      val mockLabelService = mock[LabelService]
      when(mockLabelService.findById(any[Long])) thenReturn Future {
        Some(label)
      }
      val controller = new LabelController(stubControllerComponents(), mockLabelService)

      val result: Future[Result] = controller.getOne(label.id).apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonLabel = Json.toJson(label)
      jsonBody mustBe jsonLabel
    }

    "return Not found if label with given id doesn't exist" in {

      val labelId = 1

      val fakeRequest = FakeRequest()
      val mockLabelService = mock[LabelService]
      when(mockLabelService.findById(any[Long])) thenReturn Future {
        None
      }
      val controller = new LabelController(stubControllerComponents(), mockLabelService)

      val result: Future[Result] = controller.getOne(labelId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

}
