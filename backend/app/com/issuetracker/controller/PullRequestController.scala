package com.issuetracker.controller

import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.dto.PostPullRequest
import com.issuetracker.service.PullRequestService
import play.api.Logger

class PullRequestController(
    val cc: ControllerComponents,
    val pullRequestService: PullRequestService)(implicit val executionContext: ExecutionContext)
    extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass)

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalPullRequest = request.body.validate[PostPullRequest]
    optionalPullRequest map { postPullRequest =>
      pullRequestService.insert(postPullRequest).map { result =>
        Created(Json.toJson(result))
      } recover {
        case err =>
          logger.error(err.getMessage, err)
          BadRequest("Something went wrong.")
      }
    } getOrElse {
      logger.error("Invalid pull request data.")
      Future { BadRequest("Invalid pull request data.") }
    }
  }

  def get(id: Long): Action[AnyContent] = Action.async {
    pullRequestService.get(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }

  def getByRepositoryId(id: Long): Action[AnyContent] = Action.async {
    pullRequestService.getByRepositoryId(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }

  def delete(id: Long) = Action.async {
    pullRequestService.delete(id) map {
      case x if x < 1 => NotFound
      case _          => Ok
    }
  }
}
