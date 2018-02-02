package service

import com.github.t3hnar.bcrypt.Password
import com.issuetracker.model.User
import com.issuetracker.repository.UserRepository
import com.issuetracker.service.UserService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.postgresql.util.{PSQLException, PSQLState}
import org.scalatest.Matchers._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserServiceSpec extends PlaySpec with MockitoSugar {

  "UserService#register" should {
    "return new user's data for valid registration data" in {
      val newUser = User(
        -1,
        "pera",
        "testtest",
        "Pera",
        "Peric",
        "pera@example.com",
        ""
      )
      val registeredUser = User(
        1,
        "pera",
        "testtest".bcrypt,
        "Pera",
        "Peric",
        "pera@example.com",
        "refreshtoken"
      )
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.insert(any[User])) thenReturn Future { registeredUser }
      val service = UserService(mockUserRepository)
      service.register(newUser) map { returnedUser =>
        returnedUser.id mustBe registeredUser.id
        returnedUser.username mustBe registeredUser.username
        returnedUser.firstName mustBe registeredUser.firstName
        returnedUser.lastName mustBe registeredUser.lastName
        returnedUser.email mustBe registeredUser.email
      }
    }

    "throw PSQLException when user with same username or email address already exists" in {
      val newUser = User(
        -1,
        "pera",
        "testtest",
        "Pera",
        "Peric",
        "pera@example.com",
        ""
      )
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.insert(any[User])) thenReturn Future {
        throw new PSQLException("Unique key constraint violated.", PSQLState.DATA_ERROR)
      }
      val service = UserService(mockUserRepository)
      ScalaFutures.whenReady(service.register(newUser).failed) { e =>
        e shouldBe an[PSQLException]
      }
    }

  }

}
