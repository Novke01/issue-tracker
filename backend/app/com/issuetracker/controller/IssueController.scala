package com.issuetracker.controller

import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.dto.PostIssue
import com.issuetracker.service.{AssignedUserService, IssueService}
import play.api.Logger

class IssueController(val cc: ControllerComponents,
                      val issueService: IssueService,
                      val assignedUserService: AssignedUserService)
                    (implicit val executionContext: ExecutionContext)
  extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass())

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalRepository = request.body.validate[PostIssue]
    Future {
      BadRequest
    }
    optionalRepository match {
      case JsSuccess(newIssue: PostIssue, _) =>
        assignedUserService.insert(newIssue).map { result =>
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

}