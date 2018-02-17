package com.issuetracker.controller

import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.dto.PostComment
import com.issuetracker.service.CommentService
import play.api.Logger

class CommentController(val cc: ControllerComponents, val commentService: CommentService)(
    implicit val executionContext: ExecutionContext)
    extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass())

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalPostComment = request.body.validate[PostComment]
    optionalPostComment map { postComment =>
      commentService.insert(postComment).map { result =>
        Created(Json.toJson(result))
      } recover {
        case err =>
          logger.error(err.getMessage, err)
          BadRequest("Something went wrong.")
      }
    } getOrElse {
      logger.error("Invalid comment data.")
      Future { BadRequest("Invalid comment data.") }
    }
  }

  def get(id: Long): Action[AnyContent] = Action.async {
    commentService.get(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }

  def getByIssueId(id: Long): Action[AnyContent] = Action.async {
    commentService.getByIssueId(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }

  def getByPullRequestId(id: Long): Action[AnyContent] = Action.async {
    commentService.getByPullRequestId(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }
}
