package com.issuetracker.controller

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import com.issuetracker.dto.RegisterUser
import com.issuetracker.dto.RegisteredUser
import com.issuetracker.service.UserService

import play.api.Logger
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents

class UserController(
    val cc: ControllerComponents,
    val userService: UserService
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass)

  def register: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalUser = request.body.validate[RegisterUser]
    optionalUser map { registerUser =>
      userService.register(registerUser) map { result =>
        logger.info("user registered successfully")
        val user: RegisteredUser = result
        Created(Json.toJson(user))
      } recover {
        case err =>
          logger.error(err.getMessage, err)
          BadRequest("Something went wrong.")
      }
    } getOrElse {
      logger.error("Invalid registration data.")
      Future { BadRequest("Invalid registration data.") }
    }
  }

  def getAll: Action[AnyContent] = Action.async {
    userService.getAll map (users => Ok(Json.toJson(users)))
  }

  def getUserData(id: Long): Action[AnyContent] = Action.async { _ =>
    userService.get(id) map { result =>
      val user: RegisteredUser = result
      Ok(Json.toJson(user))
    } recover {
      case err =>
        logger.error(err.getMessage, err)
        BadRequest("User not found")
    }
  }

}
