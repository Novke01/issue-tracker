import scala.concurrent.ExecutionContext.Implicits.global

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents

import akka.stream.Materializer
import play.api.BuiltInComponents
import play.api.BuiltInComponentsFromContext
import play.api.NoHttpFiltersComponents
import play.api.routing.Router
import play.api.test.FakeRequest
import play.api.libs.json.Json
import play.api.test.Helpers._
import com.issuetracker.service.UserService
import com.issuetracker.controller.UserController
import com.issuetracker.controller.UserController
import com.issuetracker.model.User
import com.issuetracker.dto.RegisteredUser
import scala.concurrent.Future
import play.api.mvc.Result

class UserControllerSpec extends PlaySpec with MockitoSugar with OneAppPerSuiteWithComponents {
  
  override def components: BuiltInComponents = new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
      lazy val router: Router = Router.empty
  }
  
  implicit lazy val materializer: Materializer = app.materializer
  
  "UserController#register" should {
    "return registered user's data for valid data" in {
      val registeredUser = RegisteredUser(
        1,
        "pera",
        "Pera",
        "Peric",
        "pera@example.com"
      )
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "username" -> registeredUser.username,
          "password" -> "testtest",
          "firstName" -> registeredUser.firstName,
          "lastName" -> registeredUser.lastName,
          "email" -> registeredUser.email
        )
      )
      val mockUserService = mock[UserService]
      when(mockUserService.register(any[User])) thenReturn Future { registeredUser }
      val controller = new UserController(stubControllerComponents(), mockUserService)
      val result: Future[Result] = controller.register().apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonRegisteredUser = Json.toJson(registeredUser)
      jsonBody mustBe jsonRegisteredUser
    }
    
    "return Bad Request for failed registration" in {
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "username" -> "pera",
          "password" -> "testtest",
          "firstName" -> "Pera",
          "lastName" -> "Peric",
          "email" -> "pera@example.com"
        )
      )
      val mockUserService = mock[UserService]
      when(mockUserService.register(any[User])) thenReturn Future { throw new Exception }
      val controller = new UserController(stubControllerComponents(), mockUserService)
      val result: Future[Result] = controller.register().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
    
    "return Bad Request for invalid data" in {
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "username" -> "pera",
          "password" -> "testtest",
          "firstName" -> "Pera",
          "lastName" -> "Peric",
          "email" -> "pera"
        )
      )
      val mockUserService = mock[UserService]
      val controller = new UserController(stubControllerComponents(), mockUserService)
      val result: Future[Result] = controller.register().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
    
  }
  
}