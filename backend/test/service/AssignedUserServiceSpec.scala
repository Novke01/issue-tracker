package service

import com.issuetracker.model.{AssignedUser, User}
import com.issuetracker.repository.{AssignedUserRepository, IssueLabelRepository, IssueRepository}
import com.issuetracker.service.AssignedUserService
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AssignedUserServiceSpec extends PlaySpec with MockitoSugar {

  "AssignedUserService#findAssigneesByIssueId" should {
    "return all assignees for the issue with given id" in {

      val issueId = 1
      val user = User(
        1,
        "pera",
        "password",
        "Pera",
        "Peric",
        "pera@example.com",
        "refreshToken"
      )
      val mockAssignedUserRepository = mock[AssignedUserRepository]
      when(mockAssignedUserRepository.findAssigneesByIssueId(any[Int])) thenReturn Future {
        Seq(user)
      }
      val service =
        AssignedUserService(mockAssignedUserRepository, mock[IssueRepository])
      service.findAssigneesByIssueId(issueId) map { users =>
        users.length mustBe 1
        val returnedUser = users.head
        returnedUser.id mustBe user.id
        returnedUser.username mustBe user.username
        returnedUser.firstName mustBe user.firstName
        returnedUser.lastName mustBe user.lastName
        returnedUser.email mustBe user.email
      }
    }
    "return empty sequence if there is no assigned user for the issue with given id" in {

      val issueId = 1

      val mockAssignedUserRepository = mock[AssignedUserRepository]
      when(mockAssignedUserRepository.findAssigneesByIssueId(any[Int])) thenReturn Future {
        Seq()
      }
      val service =
        AssignedUserService(mockAssignedUserRepository, mock[IssueRepository])
      service.findAssigneesByIssueId(issueId) map { issues =>
        issues.length mustBe 0
      }
    }
  }

  "AssignedUserService#insertAssignee" should {

    "return newAssignee data for valid assignee data" in {
      val issueId = 1l
      val userId = 1l

      val newAssignee = AssignedUser(
        1l,
        userId,
        issueId
      )

      val mockAssignedUserRepository = mock[AssignedUserRepository]
      when(mockAssignedUserRepository.insertAssignee(any[Long], any[Long])) thenReturn Future { newAssignee }
      val service =
        AssignedUserService(mockAssignedUserRepository, mock[IssueRepository])
      service.insertAssignee(issueId, userId) map { returnedAssignedUser =>
        returnedAssignedUser.id mustBe newAssignee.id
        returnedAssignedUser.userId mustBe newAssignee.userId
        returnedAssignedUser.issueId mustBe newAssignee.issueId
      }
    }
  }

}
