import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents

import com.issuetracker.controller.AuthController
import com.issuetracker.dto.LoggedInUser
import com.issuetracker.dto.LoginUser
import com.issuetracker.service.AuthService
import com.issuetracker.util.JwtUtil

import akka.stream.Materializer
import play.api.BuiltInComponents
import play.api.BuiltInComponentsFromContext
import play.api.NoHttpFiltersComponents
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.routing.Router
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.mvc.ResponseHeader
import play.api.mvc.RequestHeader
import com.issuetracker.dto.JwtUser

class AuthControllerSpec extends PlaySpec with MockitoSugar with OneAppPerSuiteWithComponents {
  
  override def components: BuiltInComponents = new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
      lazy val router: Router = Router.empty
  }
  
  implicit lazy val materializer: Materializer = app.materializer
  
  "AuthControllerSpec#login" should {
    
    "return access and refresh token for valid login data" in {
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "username" -> "pera",
          "password" -> "testtest"
        )
      )
      val loggedInUser = LoggedInUser("accesstoken", "refreshtoken")
      val mockJwtUtil = mock[JwtUtil]
      val mockAuthService = mock[AuthService]
      when(mockAuthService.login(any[LoginUser])) thenReturn Future { loggedInUser }
      val controller = new AuthController(stubControllerComponents(), mockJwtUtil, mockAuthService)
      val result: Future[Result] = controller.login().apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonLoggedInUser = Json.toJson(loggedInUser)
      jsonBody mustBe jsonLoggedInUser
    }
    
    "return Bad Request for unsuccessful login" in {
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "username" -> "pera",
          "password" -> "testtest"
        )
      )
      val mockJwtUtil = mock[JwtUtil]
      val mockAuthService = mock[AuthService]
      when(mockAuthService.login(any[LoginUser])) thenReturn Future { throw new Exception }
      val controller = new AuthController(stubControllerComponents(), mockJwtUtil, mockAuthService)
      val result: Future[Result] = controller.login().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
    
    "return Bad Request for invalid data" in {
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "usernam" -> "pera",
          "passworp" -> "testtest"
        )
      )
      val mockJwtUtil = mock[JwtUtil]
      val mockAuthService = mock[AuthService]
      val controller = new AuthController(stubControllerComponents(), mockJwtUtil, mockAuthService)
      val result: Future[Result] = controller.login().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
    
  }
  
  "AuthControllerSpec#refresh" should {
    
    "return access and refresh token for valid refresh token" in {
      val jwtUser = JwtUser(
        1,
        "pera",
        "Pera",
        "Peric",
        "pera@example.com",
        100000000000000L
      )
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "token" -> "refreshtoken"
        )
      )
      val loggedInUser = LoggedInUser("accesstoken", "refreshtoken")
      val mockJwtUtil = mock[JwtUtil]
      when(mockJwtUtil.decode(any[RequestHeader])) thenReturn Option(jwtUser)
      val mockAuthService = mock[AuthService]
      when(mockAuthService.refresh(any[Int], any[String])) thenReturn Future { loggedInUser }
      val controller = new AuthController(stubControllerComponents(), mockJwtUtil, mockAuthService)
      val result: Future[Result] = controller.refresh().apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonLoggedInUser = Json.toJson(loggedInUser)
      jsonBody mustBe jsonLoggedInUser
    }
    
    "return Bad Request for unsuccessful refresh" in {
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "token" -> "refreshtoken"
        )
      )
      val mockJwtUtil = mock[JwtUtil]
      val mockAuthService = mock[AuthService]
      when(mockAuthService.refresh(any[Int], any[String])) thenReturn Future { throw new Exception }
      val controller = new AuthController(stubControllerComponents(), mockJwtUtil, mockAuthService)
      val result: Future[Result] = controller.login().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
    
    "return Bad Request for invalid data" in {
      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "refrTkn" -> "refreshtoken"
        )
      )
      val mockJwtUtil = mock[JwtUtil]
      val mockAuthService = mock[AuthService]
      val controller = new AuthController(stubControllerComponents(), mockJwtUtil, mockAuthService)
      val result: Future[Result] = controller.refresh().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
    
  }
  
}