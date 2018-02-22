package com.issuetracker.controller

import com.issuetracker.dto.PostWikiPage

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.issuetracker.service.WikiPageService
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

class WikiPageController(
    val cc: ControllerComponents,
    val wikiPageService: WikiPageService
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass())

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalWikiPage = request.body.validate[PostWikiPage]
    optionalWikiPage map { postWikiPage =>
      wikiPageService.insert(postWikiPage).map { result =>
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

  def get(id: Long): Action[AnyContent] = Action.async {
    wikiPageService.get(id) map {
      case Some(repository) => Ok(Json.toJson(repository))
      case None             => NotFound
    }
  }

  def delete(id: Long) = Action.async {
    wikiPageService.delete(id) map {
      case x if x < 1 => NotFound
      case _          => Ok
    }
  }
}
