package com.issuetracker.controller

import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.dto.PostIssue
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

  def getOne(id: Long): Action[AnyContent] = Action.async {
    issueService.findById(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }



}