package com.issuetracker.controller

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import com.issuetracker.dto.LoginUser
import com.issuetracker.dto.RegisterUser
import com.issuetracker.service.UserService

import play.api.Logger
import play.api.libs.json._
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.ControllerComponents
import com.issuetracker.dto.RefreshToken

class UserController(val cc: ControllerComponents, val userService: UserService)
  (implicit val executionContext: ExecutionContext) 
  extends AbstractController(cc) {
  
  val logger: Logger = Logger(this.getClass())

  def register: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalUser = request.body.validate[RegisterUser]
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
  
  def login: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalUser = request.body.validate[LoginUser]
    optionalUser match {
      case JsSuccess(loginUser: LoginUser, _) =>
        userService.login(loginUser).map { result =>
          Ok(Json.toJson(result))
        } recover {
          case _ =>
            BadRequest("Something went wrong.")
        }
      case JsError(_) => Future {
        BadRequest("Invalid log in data.")
      }
    }
  }
  
  def refreshToken(id: Long): Action[JsValue] = Action.async(parse.json) { request => 
    val optionToken = request.body.validate[RefreshToken]
    optionToken match {
      case JsSuccess(refreshToken: RefreshToken, _) =>
        userService.refreshAccessToken(id, refreshToken.token).map { loggedInUser =>
          Ok(Json.toJson(loggedInUser))
        } recover {
          case _ =>
            Unauthorized("Invalid data.")
        }
      case JsError(_) => Future {
        BadRequest("Invalid data format.")
      }
    }
  }
  
  def hello = Action(parse.json) {
    Ok("{\"text\": \"Hello, World!\"}")
  }
  
}