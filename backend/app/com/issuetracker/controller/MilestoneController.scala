package com.issuetracker.controller

import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.dto.PostMilestone
import com.issuetracker.service.MilestoneService
import play.api.Logger

class MilestoneController(val cc: ControllerComponents, val milestoneService: MilestoneService)(
    implicit val executionContext: ExecutionContext)
    extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass)

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalMilestone = request.body.validate[PostMilestone]
    optionalMilestone map { postMilestone =>
      milestoneService.insert(postMilestone).map { result =>
        Created(Json.toJson(result))
      } recover {
        case err =>
          logger.error(err.getMessage, err)
          BadRequest("Something went wrong.")
      }
    } getOrElse {
      logger.error("Invalid milestone data.")
      Future { BadRequest("Invalid milestone data.") }
    }
  }

  def get(id: Long): Action[AnyContent] = Action.async {
    milestoneService.get(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }

  def getByRepositoryId(id: Long): Action[AnyContent] = Action.async {
    milestoneService.getByRepositoryId(id).map { result =>
      Ok(Json.toJson(result))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("Something went wrong.")
    }
  }
}
