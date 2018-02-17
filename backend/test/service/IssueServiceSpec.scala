package service

import com.issuetracker.dto.PostIssue
import com.issuetracker.model.{Issue, IssueStatus}
import com.issuetracker.repository.{AssignedUserRepository, IssueLabelRepository, IssueRepository}
import com.issuetracker.service.IssueService
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import java.lang.System.currentTimeMillis

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class IssueServiceSpec extends PlaySpec with MockitoSugar {

  "IssueService#create" should {

    "return newIssue data for valid postIssue data" in {
      val postIssue = PostIssue(
        1l,
        "newIssueTitle",
        "newIssueDescription",
        1l,
        List[Long](),
        List[Long](),
        Some(1)
      )

      val newIssue = Issue(
        1l,
        1l,
        "newIssueTitle",
        "newIssueDescription",
        currentTimeMillis(),
        1l,
        IssueStatus.OPENED,
        Some(1)
      )

      val mockIssueRepository        = mock[IssueRepository]
      val mockIssueLabelRepository   = mock[IssueLabelRepository]
      val mockAssignedUserRepository = mock[AssignedUserRepository]
      when(mockIssueRepository.insert(any[Issue])) thenReturn Future { newIssue }
      val service =
        IssueService(mockIssueRepository, mockIssueLabelRepository, mockAssignedUserRepository)
      service.insert(postIssue, postIssue.assignees, postIssue.labels) map { returnedIssue =>
        returnedIssue.id mustBe newIssue.id
        returnedIssue.repositoryId mustBe newIssue.repositoryId
        returnedIssue.title mustBe newIssue.title
        returnedIssue.description mustBe newIssue.description
        returnedIssue.status mustBe newIssue.status
      }
    }
  }

  "IssueService#update" should {

    "return updated issue" in {

      val issue = Issue(
        1,
        1,
        "title",
        "description",
        currentTimeMillis(),
        1,
        IssueStatus.OPENED,
        Some(1)
      )

      val updatedIssue = Issue(
        1,
        1,
        "updatedTitle",
        "updatedDescription",
        issue.created,
        1,
        IssueStatus.CLOSED,
        Some(1)
      )

      val mockIssueRepository = mock[IssueRepository]
      when(mockIssueRepository.findById(updatedIssue.id)) thenReturn Future {
        Some(issue)
      }
      when(mockIssueRepository.update(any[Issue])) thenReturn Future {
        Some(updatedIssue)
      }

      val service =
        IssueService(mockIssueRepository, mock[IssueLabelRepository], mock[AssignedUserRepository])
      service.update(updatedIssue) map { issue =>
        issue mustBe updatedIssue
      }
    }
  }

  "IssueService#findById" should {
    "return issue with given id" in {

      val issueId = 1
      val issue = Issue(
        1,
        1,
        "title",
        "description",
        currentTimeMillis(),
        1,
        IssueStatus.OPENED,
        Some(1)
      )
      val mockIssueRepository = mock[IssueRepository]
      when(mockIssueRepository.findById(any[Int])) thenReturn Future {
        Some(issue)
      }
      val service =
        IssueService(mockIssueRepository, mock[IssueLabelRepository], mock[AssignedUserRepository])
      service.findById(issueId) map { returnedIssue =>
        returnedIssue.get.id mustBe issue.id
        returnedIssue.get.repositoryId mustBe issue.repositoryId
        returnedIssue.get.title mustBe issue.title
        returnedIssue.get.description mustBe issue.description
        returnedIssue.get.created mustBe issue.created
        returnedIssue.get.ownerId mustBe issue.ownerId
        returnedIssue.get.status mustBe issue.status
      }
    }
    "return None if the issue with given id doesn't exist" in {

      val issueId = 1

      val mockIssueRepository = mock[IssueRepository]
      when(mockIssueRepository.findById(any[Int])) thenReturn Future {
        None
      }
      val service =
        IssueService(mockIssueRepository, mock[IssueLabelRepository], mock[AssignedUserRepository])
      service.findById(issueId) map { issue =>
        issue mustBe None
      }
    }
  }

  "IssueService#findByRepositoryId" should {
    "return all issues for the repository with given id" in {

      val repositoryId = 1
      val issue = Issue(
        1,
        1,
        "title",
        "description",
        currentTimeMillis(),
        1,
        IssueStatus.OPENED,
        Some(1)
      )
      val mockIssueRepository = mock[IssueRepository]
      when(mockIssueRepository.findByRepositoryId(any[Int])) thenReturn Future {
        Seq(issue)
      }
      val service =
        IssueService(mockIssueRepository, mock[IssueLabelRepository], mock[AssignedUserRepository])
      service.findByRepositoryId(repositoryId) map { issues =>
        issues.length mustBe 1
        val returnedIssue = issues.head
        returnedIssue.id mustBe issue.id
        returnedIssue.repositoryId mustBe issue.repositoryId
        returnedIssue.title mustBe issue.title
        returnedIssue.description mustBe issue.description
        returnedIssue.created mustBe issue.created
        returnedIssue.ownerId mustBe issue.ownerId
        returnedIssue.status mustBe issue.status
      }
    }
    "return empty sequence if there is no issue for the repository with given id" in {

      val repositoryId = 1

      val mockIssueRepository = mock[IssueRepository]
      when(mockIssueRepository.findByRepositoryId(any[Int])) thenReturn Future {
        Seq()
      }
      val service =
        IssueService(mockIssueRepository, mock[IssueLabelRepository], mock[AssignedUserRepository])
      service.findByRepositoryId(repositoryId) map { issues =>
        issues.length mustBe 0
      }
    }
  }

}
