package com.issuetracker.controller

import com.issuetracker.dto.PostLabel
import com.issuetracker.service.LabelService
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class LabelController(val cc: ControllerComponents, val labelService: LabelService)(
    implicit val executionContext: ExecutionContext)
    extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass)

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalLabel = request.body.validate[PostLabel]
    Future {
      BadRequest
    }
    optionalLabel match {
      case JsSuccess(postLabel: PostLabel, _) =>
        labelService.insert(postLabel).map { result =>
          Created(Json.toJson(result))
        } recover {
          case err =>
            logger.error(err.getMessage, err)
            BadRequest("Something went wrong.")
        }
      case _: JsError =>
        Future {
          logger.error("Invalid label data.")
          BadRequest("Invalid label data.")
        }
    }
  }

  def getOne(id: Long): Action[AnyContent] = Action.async {
    labelService.findById(id) map {
      case Some(label) => Ok(Json.toJson(label))
      case None        => NotFound
    }
  }

  def remove(labelId: Long): Action[AnyContent] = Action.async {
    labelService.remove(labelId: Long) map { result =>
      Ok(Json.toJson(result))
    }
  }

}
