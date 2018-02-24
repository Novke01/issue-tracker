package service

import com.issuetracker.model.User
import com.issuetracker.repository.ContributorRepository
import com.issuetracker.service.ContributorService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mockito.invocation.InvocationOnMock
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatest.concurrent.Eventually

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ContributorServiceSpec extends PlaySpec with MockitoSugar with Eventually {

  "ContributorService#updateContributors" should {

    "create new contributors while not touching the existing ones" in {

      val repositoryId = 1
      val contributors = List(1L, 2L, 3L)
      val currentContributors = List(
        User(1, null, null, null, null, null, null),
        User(3, null, null, null, null, null, null)
      )

      val mockContributorRepository = mock[ContributorRepository]
      when(mockContributorRepository.getContributorsByRepositoryId(repositoryId)) thenReturn Future {
        currentContributors
      }

      var contributorsToBeSaved: List[Long] = null
      when(mockContributorRepository.addContributors(any[Long], any[List[Long]])) thenAnswer ((invocation: InvocationOnMock) => {
        contributorsToBeSaved = invocation.getArgument(1)
      })

      val service = ContributorService(mockContributorRepository)
      service.updateContributors(repositoryId, contributors)

      eventually { contributorsToBeSaved mustBe List(2L) }
    }
  }
}
