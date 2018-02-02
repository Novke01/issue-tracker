package service

import com.issuetracker.model.{Repository, User}
import com.issuetracker.repository.RepositoryRepository
import com.issuetracker.service.RepositoryService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.github.t3hnar.bcrypt.Password

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
      when(mockRepositoryRepository.findByOwnerId(any[Int])) thenReturn Future { Seq(repository) }
      val service = RepositoryService(mockRepositoryRepository)
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
      when(mockRepositoryRepository.findByOwnerId(any[Int])) thenReturn Future { Seq() }
      val service = RepositoryService(mockRepositoryRepository)
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
      val service = RepositoryService(mockRepositoryRepository)
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
      when(mockRepositoryRepository.findByContributorId(any[Int])) thenReturn Future { Seq() }
      val service = RepositoryService(mockRepositoryRepository)
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
      when(mockRepositoryRepository.get(any[Int])) thenReturn Future { Some(repository) }
      val service = RepositoryService(mockRepositoryRepository)
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
      when(mockRepositoryRepository.get(any[Int])) thenReturn Future { None }
      val service = RepositoryService(mockRepositoryRepository)
      service.get(repoId) map { repository =>
        repository mustBe None
      }
    }
  }

  "RepositoryService#getRepositoryOwner" should {
    "return owner of the repository with given id" in {

      val repoId  = 1
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
      when(mockRepositoryRepository.getRepositoryOwner(any[Int])) thenReturn Future { Some(user) }
      val service = RepositoryService(mockRepositoryRepository)
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
      when(mockRepositoryRepository.getRepositoryOwner(any[Int])) thenReturn Future { None }
      val service = RepositoryService(mockRepositoryRepository)
      service.getRepositoryOwner(repoId) map { user =>
        user mustBe None
      }
    }
  }

}
