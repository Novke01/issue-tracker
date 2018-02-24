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

import com.github.t3hnar.bcrypt.Password
import com.issuetracker.model.User
import com.issuetracker.repository.UserRepository
import com.issuetracker.service.UserService
import org.postgresql.util.PSQLException
import org.postgresql.util.PSQLState
import org.scalatest.concurrent.ScalaFutures
import com.issuetracker.exception.UserNotFoundException
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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
  
  "UserService#get" should {
    
    "returns user's data for existing user id" in {
      val id = 1
      val userData = User(
        id,
        "pera",
        "testtest".bcrypt,
        "Pera",
        "Peric",
        "pera@example.com",
        "refreshtoken"
      )
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.findById(any[Long])) thenReturn Future {
        Option(userData)
      }
      val service = UserService(mockUserRepository)
      service.get(id) map { result => 
        result.id mustBe userData.id
        result.username mustBe userData.username
        result.firstName mustBe userData.firstName
        result.lastName mustBe userData.lastName
        result.email mustBe userData.email
      }
    }
    
    "throw UserNotFoundException when user with given id doesn't exist" in {
      val id = -1
      val mockUserRepository = mock[UserRepository]
      when(mockUserRepository.findById(any[Long])) thenReturn Future {
        throw new UserNotFoundException("User doesn't exist")
      }
      val service = UserService(mockUserRepository)
      ScalaFutures.whenReady(service.get(id).failed) { e =>
        e shouldBe an [UserNotFoundException]
      }
    }
    
  }
  
}
