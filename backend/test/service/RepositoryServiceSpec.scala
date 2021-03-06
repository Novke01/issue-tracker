package service

import com.github.t3hnar.bcrypt.Password
import com.issuetracker.model.{Repository, User}
import com.issuetracker.repository.RepositoryRepository
import com.issuetracker.service.{ContributorService, RepositoryService}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RepositoryServiceSpec extends PlaySpec with MockitoSugar {

  "RepositoryService#findByOwnerId" should {
    "return all repositories that is owned by user with given id" in {

      val ownerId = 1
      val repository = Repository(
        1,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        ownerId
      )
      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.findByOwnerId(any[Int])) thenReturn Future {
        Seq(repository)
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.findByOwnerId(ownerId) map { repositories =>
        repositories.length mustBe 1
        val returnedRepo = repositories.head
        returnedRepo.id mustBe repository.id
        returnedRepo.name mustBe repository.name
        returnedRepo.url mustBe repository.url
        returnedRepo.description mustBe repository.description
        returnedRepo.ownerId mustBe repository.ownerId
      }
    }
    "return empty sequence if the user with given id isn't owner of any repository" in {

      val ownerId = 1

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.findByOwnerId(any[Int])) thenReturn Future {
        Seq()
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.findByOwnerId(ownerId) map { repositories =>
        repositories.length mustBe 0
      }
    }
  }

  "RepositoryService#findByContributorId" should {
    "return all repositories that is contributed by user with given id" in {

      val contributorId = 1
      val repository = Repository(
        1,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        2
      )
      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.findByContributorId(any[Int])) thenReturn Future {
        Seq(repository)
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.findByContributorId(contributorId) map { repositories =>
        repositories.length mustBe 1
        val returnedRepo = repositories.head
        returnedRepo.id mustBe repository.id
        returnedRepo.name mustBe repository.name
        returnedRepo.url mustBe repository.url
        returnedRepo.description mustBe repository.description
        returnedRepo.ownerId mustBe repository.ownerId
      }
    }
    "return empty sequence if the user with given id isn't contributor of any repository" in {

      val contributorId = 1

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.findByContributorId(any[Int])) thenReturn Future {
        Seq()
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.findByContributorId(contributorId) map { repositories =>
        repositories.length mustBe 0
      }
    }
  }

  "RepositoryService#get" should {
    "return repository with given id" in {

      val repoId = 1
      val repository = Repository(
        repoId,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        1
      )
      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.get(any[Int])) thenReturn Future {
        Some(repository)
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.get(repoId) map { returnedRepository =>
        returnedRepository.get.id mustBe repository.id
        returnedRepository.get.name mustBe repository.name
        returnedRepository.get.url mustBe repository.url
        returnedRepository.get.description mustBe repository.description
        returnedRepository.get.ownerId mustBe repository.ownerId
      }
    }
    "return None if the repository with given id doesn't exist" in {

      val repoId = 1

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.get(any[Int])) thenReturn Future {
        None
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.get(repoId) map { repository =>
        repository mustBe None
      }
    }
  }

  "RepositoryService#getRepositoryOwner" should {
    "return owner of the repository with given id" in {

      val repoId = 1
      val ownerId = 1

      val user = User(
        ownerId,
        "pera",
        "testtest".bcrypt,
        "Pera",
        "Peric",
        "pera@example.com",
        "refreshtoken"
      )

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.getRepositoryOwner(any[Int])) thenReturn Future {
        Some(user)
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.getRepositoryOwner(repoId) map { returnedUser =>
        returnedUser.get.id mustBe user.id
        returnedUser.get.firstName mustBe user.firstName
        returnedUser.get.lastName mustBe user.lastName
        returnedUser.get.username mustBe user.username
        returnedUser.get.email mustBe user.email
      }
    }
    "return None if the repository with given doesn't have owner" in {

      val repoId = 1

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.getRepositoryOwner(any[Int])) thenReturn Future {
        None
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.getRepositoryOwner(repoId) map { user =>
        user mustBe None
      }
    }
  }

  "RepositoryService#update" should {

    "return updated repository" in {

      val repository = Repository(
        1,
        "Issue",
        "github.com",
        "Description",
        1
      )

      val updatedRepository = Repository(
        1,
        "IssueTracker",
        "github.com",
        "Description",
        1
      )

      val contributors = List(1L, 2L)
      val currentUserId = 1

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.get(updatedRepository.id)) thenReturn Future {
        Some(repository)
      }
      when(mockRepositoryRepository.update(any[Repository])) thenReturn Future {
        Some(updatedRepository)
      }

      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.update(updatedRepository, contributors, currentUserId) map { repository =>
        repository mustBe updatedRepository
      }
    }

    "throw IllegalArgumentException if a user tries to update a repository he is not the owner of" in {

      val repository = Repository(
        1,
        "Issue",
        "github.com",
        "Description",
        1
      )

      val updatedRepository = Repository(
        1,
        "IssueTracker",
        "github.com",
        "Description",
        1
      )

      val contributors = List(1L, 2L)
      val currentUserId = 2

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.get(updatedRepository.id)) thenReturn Future {
        Some(repository)
      }
      when(mockRepositoryRepository.update(any[Repository])) thenReturn Future {
        Some(updatedRepository)
      }

      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])

      assertThrows[IllegalArgumentException] {
        service.update(updatedRepository, contributors, currentUserId)
      }
    }

    "throw IllegalArgumentException if the repository doesn't exist" in {

      val updatedRepository = Repository(
        1,
        "IssueTracker",
        "github.com",
        "Description",
        1
      )

      val contributors = List(1L, 2L)
      val currentUserId = 1

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.get(updatedRepository.id)) thenReturn Future {
        None
      }
      when(mockRepositoryRepository.update(any[Repository])) thenReturn Future {
        Some(updatedRepository)
      }

      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])

      assertThrows[IllegalArgumentException] {
        service.update(updatedRepository, contributors, currentUserId)
      }
    }
  }

  "RepositoryService#delete" should {
    "return 1 if delete repository with given id" in {

      val repoId = 1

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.delete(any[Int])) thenReturn Future {
        1
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.delete(repoId) map { rows =>
        rows mustBe 1
      }
    }
    "return 0 if the repository with given id doesn't exist" in {

      val repoId = 1

      val mockRepositoryRepository = mock[RepositoryRepository]
      when(mockRepositoryRepository.delete(any[Int])) thenReturn Future {
        0
      }
      val service = RepositoryService(mockRepositoryRepository, mock[ContributorService])
      service.delete(repoId) map { rows =>
        rows mustBe 0
      }
    }
  }
}
