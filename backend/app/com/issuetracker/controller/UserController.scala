package com.issuetracker.controller

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import com.issuetracker.dto.RegisterUser
import com.issuetracker.service.UserService

import play.api.Logger
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.ControllerComponents

class UserController(val cc: ControllerComponents, val userService: UserService)
  (implicit val executionContext: ExecutionContext) 
  extends AbstractController(cc) {
  
  val logger: Logger = Logger(this.getClass())

  def register: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalUser = request.body.validate[RegisterUser]
    Future {
      BadRequest
    }
    optionalUser match {
      case JsSuccess(registerUser: RegisterUser, _) => 
        userService.register(registerUser).map { result => 
          Created(Json.toJson(result)) 
        } recover {
          case err =>
            logger.error(err.getMessage, err)
            BadRequest("Something went wrong.")
        }
      case err: JsError => Future {
        logger.error("Invalid registration data.")
        BadRequest("Invalid registration data.")
      }
    }
  }
  
  def hello = Action(parse.json) {
    Ok("{\"text\": \"Hello, World!\"}")
  }
  
}