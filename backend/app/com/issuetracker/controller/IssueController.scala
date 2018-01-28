package com.issuetracker.controller

import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.dto.{PostIssue, UpdateIssue}
import com.issuetracker.service.{AssignedUserService, IssueService}
import com.issuetracker.util.JwtUtil
import play.api.Logger

class IssueController(val cc: ControllerComponents,
                      val issueService: IssueService,
                      val assignedUserService: AssignedUserService,
                      val jwtUtil: JwtUtil)
                    (implicit val executionContext: ExecutionContext)
  extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass())

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalIssue = request.body.validate[PostIssue]
    Future {
      BadRequest
    }
    optionalIssue match {
      case JsSuccess(postIssue: PostIssue, _) =>
        val ownerId: Long = jwtUtil.decode(request) map { _.id } getOrElse { -1 }
        val modifiedPostIssue = postIssue.copy(ownerId = ownerId )
        issueService.insert(modifiedPostIssue).map { result =>
          Created(Json.toJson(result))
        } recover {
          case err =>
            logger.error(err.getMessage, err)
            BadRequest("Something went wrong.")
        }
      case _: JsError => Future {
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
        case notFoundError : IllegalArgumentException =>
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

  def insertAssignee(issueId: Long, userId: Long) = Action.async {
    assignedUserService.insertAssignee(issueId, userId) map { result =>
      Ok("")
    }
  }

  def removeAssignee(issueId: Long, userId: Long) = Action.async {
    assignedUserService.removeAssignee(issueId, userId) map { result =>
      Ok(Json.toJson(result))
    }
  }

  def getAssignees(issueId: Long) = Action.async {
    assignedUserService.findAssigneesByIssueId(issueId) map { result =>
      Ok(Json.toJson(result))
    }
  }

}