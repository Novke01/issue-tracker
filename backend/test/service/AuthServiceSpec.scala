package service

import com.github.t3hnar.bcrypt.Password
import com.issuetracker.dto.{JwtUser, LoginUser}
import com.issuetracker.exception.{
  IncorrectPasswordException,
  InvalidRefreshTokenException,
  UserNotFoundException
}
import com.issuetracker.model.User
import com.issuetracker.repository.UserRepository
import com.issuetracker.service.AuthService
import com.issuetracker.util.JwtUtil
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.Matchers._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuthServiceSpec extends PlaySpec with MockitoSugar {

  "AuthService#login" should {

    "return tokens for valid login data" in {
      val accessToken  = "jwttoken"
      val refreshToken = "refreshtoken"
      val password     = "testtest"
      val user = User(
        1,
        "pera",
        password.bcrypt,
        "Pera",
        "Peric",
        "pera@example.com",
        refreshToken
      )
      val loginUser          = LoginUser(user.username, password)
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.findByUsername(any[String])) thenReturn Future { Option(user) }
      val mockJwtUtil = mock[JwtUtil]
      when(mockJwtUtil.generate(any[JwtUser])) thenReturn accessToken
      val service = AuthService(mockUserRepository, mockJwtUtil)
      service.login(loginUser) map { loggedInUser =>
        loggedInUser.accessToken mustBe "jwttoken"
        loggedInUser.refreshToken mustBe refreshToken
      }
    }

    "throw IncorrectPasswordException if user sent invalid password" in {
      val password = "testtest"
      val user = User(
        1,
        "pera",
        "testtest".bcrypt,
        "Pera",
        "Peric",
        "pera@example.com",
        "refreshtoken"
      )
      val loginUser          = LoginUser(user.username, "testtestt")
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.findByUsername(any[String])) thenReturn Future { Option(user) }
      val mockJwtUtil = mock[JwtUtil]
      val service     = AuthService(mockUserRepository, mockJwtUtil)
      ScalaFutures.whenReady(service.login(loginUser).failed) { e =>
        e shouldBe an[IncorrectPasswordException]
      }
    }

    "throw UserNotFoundException if user with specified username doesn't exist" in {
      val loginUser          = LoginUser("zika", "testtest")
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.findByUsername(any[String])) thenReturn Future { None }
      val mockJwtUtil = mock[JwtUtil]
      val service     = AuthService(mockUserRepository, mockJwtUtil)
      ScalaFutures.whenReady(service.login(loginUser).failed) { e =>
        e shouldBe an[UserNotFoundException]
      }
    }

  }

  "AuthService#refresh" should {

    "return new tokens for valid refresh token and existing user id" in {
      val id           = 1
      val accessToken  = "accesstoken"
      val refreshToken = "refreshtoken"
      val user = User(
        id,
        "pera",
        "testtest".bcrypt,
        "Pera",
        "Peric",
        "pera@example.com",
        refreshToken
      )
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.findById(any[Int])) thenReturn Future { Option(user) }
      val mockJwtUtil = mock[JwtUtil]
      when(mockJwtUtil.generate(any[JwtUser])) thenReturn accessToken
      val service = AuthService(mockUserRepository, mockJwtUtil)
      service.refresh(id, refreshToken) map { loggedInUser =>
        loggedInUser.accessToken mustBe accessToken
        loggedInUser.refreshToken mustBe refreshToken
      }
    }

    "throw InvalidRefreshTokenException if invalid refresh token was sent" in {
      val id           = 1
      val refreshToken = "refreshtoken"
      val user = User(
        id,
        "pera",
        "testtest".bcrypt,
        "Pera",
        "Peric",
        "pera@example.com",
        "refreshtokenn"
      )
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.findById(any[Int])) thenReturn Future { Option(user) }
      val mockJwtUtil = mock[JwtUtil]
      val service     = AuthService(mockUserRepository, mockJwtUtil)
      ScalaFutures.whenReady(service.refresh(id, refreshToken).failed) { e =>
        e shouldBe an[InvalidRefreshTokenException]
      }
    }

    "throw UserNotFoundException if user with given id doesn't exist" in {
      val id                 = 2
      val refreshToken       = "refreshtoken"
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.findById(any[Int])) thenReturn Future { None }
      val mockJwtUtil = mock[JwtUtil]
      val service     = AuthService(mockUserRepository, mockJwtUtil)
      ScalaFutures.whenReady(service.refresh(id, refreshToken).failed) { e =>
        e shouldBe an[UserNotFoundException]
      }
    }

  }

}
