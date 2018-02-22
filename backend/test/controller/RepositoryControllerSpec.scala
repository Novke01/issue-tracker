package controller

import akka.stream.Materializer
import com.issuetracker.controller.RepositoryController
import com.issuetracker.dto._
import com.issuetracker.model.{Repository, User}
import com.issuetracker.service._
import com.issuetracker.util.JwtUtil
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api.{BuiltInComponents, BuiltInComponentsFromContext, NoHttpFiltersComponents}
import play.api.libs.json.Json
import play.api.mvc.{RequestHeader, Result}
import play.api.routing.Router
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RepositoryControllerSpec
  extends PlaySpec
    with MockitoSugar
    with OneAppPerSuiteWithComponents {

  override def components: BuiltInComponents =
    new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
      lazy val router: Router = Router.empty
    }

  implicit lazy val materializer: Materializer = app.materializer

  "RepositoryController#insert" should {
    "return new repository data for valid data" in {

      val postRepository = PostRepository(
        null,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        1,
        List(2, 3)
      )

      val repository = GetRepository(
        1,
        postRepository.name,
        postRepository.url,
        postRepository.description,
        postRepository.ownerId
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "name" -> postRepository.name,
          "url" -> postRepository.url,
          "description" -> postRepository.description,
          "ownerId" -> postRepository.ownerId,
          "contributors" -> postRepository.contributors
        )
      )
      val mockRepositoryService = mock[RepositoryService]
      when(mockRepositoryService.insert(any[PostRepository])) thenReturn Future {
        repository
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonNewRepository = Json.toJson(repository)
      jsonBody mustBe jsonNewRepository
    }

    "return Bad Request for failed insert" in {

      val postRepository = PostRepository(
        null,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        1,
        List(2, 3)
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "name" -> postRepository.name,
          "url" -> postRepository.url,
          "description" -> postRepository.description,
          "ownerId" -> postRepository.ownerId,
          "contributors" -> postRepository.contributors
        )
      )
      val mockRepositoryService = mock[RepositoryService]
      when(mockRepositoryService.insert(any[PostRepository])) thenReturn Future {
        throw new Exception
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }

    "return Bad Request for invalid data" in {

      val postRepository = PostRepository(
        null,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        1,
        List(2, 3)
      )

      val fakeRequest = FakeRequest().withBody(
        Json.obj(
          "name" -> postRepository.name,
          "url" -> postRepository.url,
          "description" -> postRepository.description,
          "ownerId" -> postRepository.ownerId,
          "contributor" -> postRepository.contributors
        )
      )
      val mockContributorService = mock[ContributorService]
      val controller = new RepositoryController(stubControllerComponents(),
        mock[RepositoryService],
        mockContributorService,
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.insert().apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }

    }
  }

  "RepositoryController#update" should {
    "return updated repository data for valid data" in {

      val postRepository = PostRepository(
        Option(1),
        "repository update",
        "https://github.com/User1/repository",
        "repository1 description",
        1,
        List(2, 3)
      )

      val repository = GetRepository(
        1,
        postRepository.name,
        postRepository.url,
        postRepository.description,
        postRepository.ownerId
      )

      val jwtUser: JwtUser = User(1, null, null, null, null, null, null)

      val fakeRequest = FakeRequest().withMethod("PATCH").withBody(
        Json.obj(
          "id" -> 1,
          "name" -> postRepository.name,
          "url" -> postRepository.url,
          "description" -> postRepository.description,
          "ownerId" -> postRepository.ownerId,
          "contributors" -> postRepository.contributors
        )
      ).addAttr(JwtUser.Key, jwtUser)

      val mockRepositoryService = mock[RepositoryService]
      when(mockRepositoryService.update(any[Repository], any[List[Long]], any[Int])) thenReturn Future {
        repository
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.update().apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonUpdatedRepository = Json.toJson(repository)
      jsonBody mustBe jsonUpdatedRepository
    }
  }

  "RepositoryController#getOwned" should {
    "return all repositories that is owned by user with given id" in {

      val ownerId = 1

      val jwtUser = JwtUser(
        ownerId,
        "pera",
        "Pera",
        "Peric",
        "pera@peric.com",
        1
      )
      val repository = GetRepository(
        1,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        ownerId
      )

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      val mockJwtUtil = mock[JwtUtil]
      when(mockJwtUtil.decode(any[RequestHeader])) thenReturn Some(jwtUser)
      when(mockRepositoryService.findByOwnerId(any[Long])) thenReturn Future {
        Seq(repository)
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mockJwtUtil)

      val result: Future[Result] = controller.getOwned(ownerId).apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonRepositories = Json.toJson(Seq(repository))
      jsonBody mustBe jsonRepositories
    }

    "return empty sequence of repositories if the user with given id isn't owner of any repository" in {

      val ownerId = 1

      val jwtUser = JwtUser(
        ownerId,
        "pera",
        "Pera",
        "Peric",
        "pera@peric.com",
        1
      )

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      val mockJwtUtil = mock[JwtUtil]
      when(mockJwtUtil.decode(any[RequestHeader])) thenReturn Some(jwtUser)
      when(mockRepositoryService.findByOwnerId(any[Long])) thenReturn Future {
        Seq[GetRepository]()
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mockJwtUtil)

      val result: Future[Result] = controller.getOwned(ownerId).apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonRepositories = Json.toJson(Seq[GetRepository]())
      jsonBody mustBe jsonRepositories
    }

    "return bad request for invalid data" in {

      val ownerId = 1

      val jwtUser = JwtUser(
        ownerId,
        "pera",
        "Pera",
        "Peric",
        "pera@peric.com",
        1
      )

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      val mockJwtUtil = mock[JwtUtil]
      when(mockJwtUtil.decode(any[RequestHeader])) thenReturn Some(jwtUser)
      when(mockRepositoryService.findByOwnerId(any[Long])) thenReturn Future {
        throw new Exception
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mockJwtUtil)

      val result: Future[Result] = controller.getOwned(ownerId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

  "RepositoryController#getContributed" should {
    "return all repositories that is contributed by user with given id" in {

      val ownerId = 1

      val jwtUser = JwtUser(
        ownerId,
        "pera",
        "Pera",
        "Peric",
        "pera@peric.com",
        1
      )
      val repository = GetRepository(
        1,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        ownerId
      )

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      val mockJwtUtil = mock[JwtUtil]
      when(mockJwtUtil.decode(any[RequestHeader])) thenReturn Some(jwtUser)
      when(mockRepositoryService.findByContributorId(any[Long])) thenReturn Future {
        Seq(repository)
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mockJwtUtil)

      val result: Future[Result] = controller.getContributed(ownerId).apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonRepositories = Json.toJson(Seq(repository))
      jsonBody mustBe jsonRepositories
    }

    "return empty sequence of repositories if the user with given id isn't contributor of any repository" in {

      val ownerId = 1

      val jwtUser = JwtUser(
        ownerId,
        "pera",
        "Pera",
        "Peric",
        "pera@peric.com",
        1
      )

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      val mockJwtUtil = mock[JwtUtil]
      when(mockJwtUtil.decode(any[RequestHeader])) thenReturn Some(jwtUser)
      when(mockRepositoryService.findByContributorId(any[Long])) thenReturn Future {
        Seq[GetRepository]()
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mockJwtUtil)

      val result: Future[Result] = controller.getContributed(ownerId).apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonRepositories = Json.toJson(Seq[GetRepository]())
      jsonBody mustBe jsonRepositories
    }
  }

  "return bad request for invalid data" in {

    val ownerId = 1

    val jwtUser = JwtUser(
      ownerId,
      "pera",
      "Pera",
      "Peric",
      "pera@peric.com",
      1
    )

    val fakeRequest = FakeRequest()
    val mockRepositoryService = mock[RepositoryService]
    val mockJwtUtil = mock[JwtUtil]
    when(mockJwtUtil.decode(any[RequestHeader])) thenReturn Some(jwtUser)
    when(mockRepositoryService.findByContributorId(any[Long])) thenReturn Future {
      throw new Exception
    }
    val controller = new RepositoryController(stubControllerComponents(),
      mockRepositoryService,
      mock[ContributorService],
      mock[IssueService],
      mock[LabelService],
      mock[WikiPageService],
      mockJwtUtil)

    val result: Future[Result] = controller.getContributed(ownerId).apply(fakeRequest)
    result map { response =>
      response.header.status mustBe 400
    }
  }

  "RepositoryController#get" should {
    "return repository with given id" in {

      val ownerId = 1

      val repository = GetRepository(
        1,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        ownerId
      )

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      when(mockRepositoryService.get(any[Long])) thenReturn Future {
        Some(repository)
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.get(repository.id).apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonRepository = Json.toJson(repository)
      jsonBody mustBe jsonRepository
    }

    "return Not found if repository with given id doesn't exist" in {

      val repositoryId = 1

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      when(mockRepositoryService.get(any[Long])) thenReturn Future {
        None
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.get(repositoryId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

  "RepositoryController#getOwner" should {
    "return owner of the repository with given id" in {

      val ownerId = 1

      val owner = RegisteredUser(
        ownerId,
        "pera",
        "Pera",
        "Peric",
        "pera@example.com"
      )

      val repository = GetRepository(
        1,
        "repository",
        "https://github.com/User1/repository",
        "repository1 description",
        ownerId
      )

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      when(mockRepositoryService.getRepositoryOwner(any[Long])) thenReturn Future {
        Some(owner)
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.getOwner(repository.id).apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonRepository = Json.toJson(owner)
      jsonBody mustBe jsonRepository
    }

    "return Not found if repository with given id doesn't have owner" in {

      val repositoryId = 1

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      when(mockRepositoryService.getRepositoryOwner(any[Long])) thenReturn Future {
        None
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.getOwner(repositoryId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

  "RepositoryController#getWikiPages" should {
    "return wiki pages of the repository with given id" in {

      val repoId = 1
      val wikiPage = GetWikiPage(
        1,
        "wiki page",
        "wiki page content",
        repoId
      )

      val fakeRequest = FakeRequest()
      val mockWikiPageService = mock[WikiPageService]
      when(mockWikiPageService.findByRepositoryId(any[Long])) thenReturn Future {
        Seq(wikiPage)
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mock[RepositoryService],
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mockWikiPageService,
        mock[JwtUtil])

      val result: Future[Result] = controller.getWikiPages(repoId).apply(fakeRequest)
      val jsonBody = contentAsJson(result)
      val jsonRepository = Json.toJson(Seq(wikiPage))
      jsonBody mustBe jsonRepository
    }

    "return empty sequence of wiki pages if the repository with given id doesn't have any wiki page" in {

      val repositoryId = 1

      val fakeRequest = FakeRequest()
      val mockWikiPageService = mock[WikiPageService]
      when(mockWikiPageService.findByRepositoryId(any[Long])) thenReturn Future {
        Seq()
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mock[RepositoryService],
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mockWikiPageService,
        mock[JwtUtil])

      val result: Future[Result] = controller.getWikiPages(repositoryId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }

  "RepositoryController#delete" should {
    "delete repository and return OK status" in {

      val repoId = 1

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      when(mockRepositoryService.delete(any[Long])) thenReturn Future {
        1
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.delete(repoId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 200
      }
    }

    "return Not found if repository with given id doesn't exist" in {

      val repoId = 1

      val fakeRequest = FakeRequest()
      val mockRepositoryService = mock[RepositoryService]
      when(mockRepositoryService.delete(any[Long])) thenReturn Future {
        0
      }
      val controller = new RepositoryController(stubControllerComponents(),
        mockRepositoryService,
        mock[ContributorService],
        mock[IssueService],
        mock[LabelService],
        mock[WikiPageService],
        mock[JwtUtil])

      val result: Future[Result] = controller.delete(repoId).apply(fakeRequest)
      result map { response =>
        response.header.status mustBe 400
      }
    }
  }
}
