package com.issuetracker.controller

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.issuetracker.service._
import com.issuetracker.util.JwtUtil
import dto.PostRepository
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.ControllerComponents
import play.api.mvc.AnyContent
import com.issuetracker.dto.JwtUser

class RepositoryController(
    val cc: ControllerComponents,
    val repositoryService: RepositoryService,
    val contributorService: ContributorService,
    val issueService: IssueService,
    val labelService: LabelService,
    val wikiPageService: WikiPageService,
    val jwtUtil: JwtUtil
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass())

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalRepository = request.body.validate[PostRepository]
    optionalRepository map { postRepository =>
      contributorService.insert(postRepository).map { result =>
        Created(Json.toJson(result))
      } recover {
        case err =>
          logger.error(err.getMessage, err)
          BadRequest("Something went wrong.")
      }
    } getOrElse {
      logger.error("Invalid repository data.")
      Future { BadRequest("Invalid repository data.") }
    }
  }

  def update: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalRepository = request.body.validate[PostRepository]
    optionalRepository map { postRepository =>
      contributorService.update(postRepository, postRepository.contributors).map { result =>
        Ok(Json.toJson(result))
      } recover {
        case notFoundError : IllegalArgumentException =>
          logger.error(notFoundError.getMessage, notFoundError)
          NotFound
        case err =>
          logger.error(err.getMessage, err)
          BadRequest("Something went wrong.")
      }
    } getOrElse {
      logger.error("Invalid repository data.")
      Future { BadRequest("Invalid repository data.") }
    }
  }

  def getOwned(id: Long): Action[AnyContent] = Action.async { request =>
    repositoryService.findByOwnerId(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }
  
  def getContributed(id: Long): Action[AnyContent] = Action.async { request => 
    repositoryService.findByContributorId(id) map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }

  def get(id: Long) = Action.async {
    repositoryService.get(id) map {
      case Some(repository) => Ok(Json.toJson(repository))
      case None             => NotFound
    }
  }

  def getContributors(id: Long) = Action.async {
    contributorService.getContributorsByRepositoryId(id) map { result =>
      Ok(Json.toJson(result))
    }
  }

  def getOwner(id: Long) = Action.async {
    repositoryService.getRepositoryOwner(id) map {
      case Some(user) => Ok(Json.toJson(user))
      case None       => NotFound
    }
  }

  def getContributorsWitchSearchTerm(repoId: Long, searchTerm: String): Action[AnyContent] =
    Action.async {
      contributorService.findByRepositoryIdAndSearchTerm(repoId, searchTerm).map { result =>
        Ok(Json.toJson(result))
      } recover {
        case err =>
          logger.error(err.getMessage, err)
          BadRequest("Something went wrong.")
      }
    }

  def getIssues(repoId: Long) = Action.async {
    issueService.findByRepositoryId(repoId) map { result =>
      Ok(Json.toJson(result))
    }
  }

  def getLabels(repoId: Long) = Action.async {
    labelService.findByRepositoryId(repoId) map { result =>
      Ok(Json.toJson(result))
    }
  }

  def getWikiPages(id: Long) = Action.async {
    wikiPageService.findByRepositoryId(id) map { result =>
      Ok(Json.toJson(result))
    }
  }

}
