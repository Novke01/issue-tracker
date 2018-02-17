package service

import com.issuetracker.model.PullRequest
import com.issuetracker.repository.PullRequestRepository
import com.issuetracker.service.PullRequestService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.issuetracker.dto.PostPullRequest
import org.joda.time.DateTime
import org.postgresql.util.{PSQLException, PSQLState}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.Matchers._

class PullRequestServiceSpec extends PlaySpec with MockitoSugar {

  "PullRequestService#getByRepositoryId" should {
    "return all pull requests that is defined in repository with given id" in {

      val repositoryId = 1
      val pullRequest = PullRequest(
        1,
        "PR title",
        "PR url",
        repositoryId
      )
      val mockPullRequest = mock[PullRequestRepository]
      when(mockPullRequest.getByRepositoryId(any[Int])) thenReturn Future { Seq(pullRequest) }
      val service = PullRequestService(mockPullRequest)
      service.getByRepositoryId(repositoryId) map { pullRequests =>
        pullRequests.length mustBe 1
        val returnedPullRequest = pullRequests.head
        returnedPullRequest.id mustBe pullRequest.id
        returnedPullRequest.title mustBe pullRequest.title
        returnedPullRequest.url mustBe pullRequest.url
        returnedPullRequest.repositoryId mustBe pullRequest.repositoryId
      }
    }
    "return empty sequence if the repository with given id doesn't have pull requests" in {

      val repositoryId = 1

      val mockMilestoneRepository = mock[PullRequestRepository]
      when(mockMilestoneRepository.getByRepositoryId(any[Int])) thenReturn Future { Seq() }
      val service = PullRequestService(mockMilestoneRepository)
      service.getByRepositoryId(repositoryId) map { pullRequests =>
        pullRequests.length mustBe 0
      }
    }
  }

  "PullRequestService#get" should {
    "return pull request with given id" in {

      val repositoryId = 1
      val prId = 1

      val pullRequest = PullRequest(
        prId,
        "PR title",
        "PR url",
        repositoryId
      )
      val mockPullRequestRepository = mock[PullRequestRepository]
      when(mockPullRequestRepository.get(any[Int])) thenReturn Future { Some(pullRequest) }
      val service = PullRequestService(mockPullRequestRepository)
      service.get(prId) map { returnedMilestone =>
        returnedMilestone.get.id mustBe pullRequest.id
        returnedMilestone.get.title mustBe pullRequest.title
        returnedMilestone.get.url mustBe pullRequest.url
        returnedMilestone.get.repositoryId mustBe pullRequest.repositoryId
      }
    }
    "return None if the pull request with given id doesn't exist" in {

      val repositoryId = 1

      val mockMilestoneRepository = mock[PullRequestRepository]
      when(mockMilestoneRepository.get(any[Int])) thenReturn Future { None }
      val service = PullRequestService(mockMilestoneRepository)
      service.get(repositoryId) map { pullRequest =>
        pullRequest mustBe None
      }
    }
  }

  "PullRequestService#save" should {
    "save pull request and return that pull request with id" in {

      val date = new DateTime()

      val pullRequest = PostPullRequest(
        "PR title",
        "PR url",
        1
      )
      val createdPullRequest = PullRequest(
        1,
        pullRequest.title,
        pullRequest.url,
        pullRequest.repositoryId
      )
      val mockPullRequest = mock[PullRequestRepository]
      when(mockPullRequest.insert(any[PullRequest])) thenReturn Future { createdPullRequest }
      val service = PullRequestService(mockPullRequest)
      service.insert(pullRequest) map { returnedPullRequest =>
        returnedPullRequest.id mustBe createdPullRequest.id
        returnedPullRequest.title mustBe createdPullRequest.title
        returnedPullRequest.url mustBe createdPullRequest.url
        returnedPullRequest.repositoryId mustBe createdPullRequest.repositoryId
      }
    }
  }
  "throw PSQLException when repository with given id doesn't exist" in {

    val postPullRequest = PostPullRequest(
      "PR title",
      "PR url",
      1
    )
    val mockPullRequestRepository = mock[PullRequestRepository]
    when(mockPullRequestRepository.insert(any[PullRequest])) thenReturn Future {
      throw new PSQLException("Foreign key constraint violated.", PSQLState.DATA_ERROR)
    }
    val service = PullRequestService(mockPullRequestRepository)
    ScalaFutures.whenReady(service.insert(postPullRequest).failed) { e =>
      e shouldBe an[PSQLException]
    }
  }
}
