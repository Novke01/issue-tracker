package com.issuetracker.controller

import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.dto.{JwtUser, PostIssue, UpdateIssue}
import com.issuetracker.service.{AssignedUserService, IssueLabelService, IssueService}
import com.issuetracker.util.JwtUtil
import play.api.Logger

class IssueController(val cc: ControllerComponents,
                      val issueService: IssueService,
                      val issueLabelService: IssueLabelService,
                      val assignedUserService: AssignedUserService,
                      val jwtUtil: JwtUtil)(implicit val executionContext: ExecutionContext)
    extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass)

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val currentUser = request.attrs
      .get(JwtUser.Key)
      .getOrElse {
        NotFound
      }
      .asInstanceOf[JwtUser]
    val optionalIssue = request.body.validate[PostIssue]
    Future {
      BadRequest
    }
    optionalIssue match {
      case JsSuccess(postIssue: PostIssue, _) =>
        val modifiedPostIssue = postIssue.copy(ownerId = currentUser.id)
        issueService.insert(modifiedPostIssue).map { result =>
          Created(Json.toJson(result))
        } recover {
          case err =>
            logger.error(err.getMessage, err)
            BadRequest("Something went wrong.")
        }
      case _: JsError =>
        Future {
          logger.error("Invalid issue data.")
          BadRequest("Invalid issue data.")
        }
    }
  }

  def update: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalRepository = request.body.validate[UpdateIssue]
    optionalRepository map { updateIssue =>
      issueService.update(updateIssue).map { result =>
        Ok(Json.toJson(result))
      } recover {
        case notFoundError: IllegalArgumentException =>
          logger.error(notFoundError.getMessage, notFoundError)
          NotFound
        case err =>
          logger.error(err.getMessage, err)
          BadRequest("Something went wrong.")
      }
    } getOrElse {
      logger.error("Invalid issue data.")
      Future { BadRequest("Invalid issue data.") }
    }
  }

  def getOne(id: Long): Action[AnyContent] = Action.async {
    issueService.findById(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }

  def insertAssignee(issueId: Long, userId: Long): Action[AnyContent] = Action.async {
    assignedUserService.insertAssignee(issueId, userId) map { _ =>
      Ok
    }
  }

  def removeAssignee(issueId: Long, userId: Long): Action[AnyContent] = Action.async {
    assignedUserService.removeAssignee(issueId, userId) map { result =>
      Ok(Json.toJson(result))
    }
  }

  def getAssignees(issueId: Long): Action[AnyContent] = Action.async {
    assignedUserService.findAssigneesByIssueId(issueId) map { result =>
      Ok(Json.toJson(result))
    }
  }

  def getLabels(issueId: Long): Action[AnyContent] = Action.async {
    issueLabelService.findLabelsByIssueId(issueId) map { result =>
      Ok(Json.toJson(result))
    }
  }

  def insertLabel(issueId: Long, labelId: Long): Action[AnyContent] = Action.async {
    issueLabelService.insertLabel(issueId, labelId) map { _ =>
      Ok
    }
  }

  def removeLabel(issueId: Long, labelId: Long): Action[AnyContent] = Action.async {
    issueLabelService.removeLabel(issueId, labelId) map { result =>
      Ok(Json.toJson(result))
    }
  }

}
