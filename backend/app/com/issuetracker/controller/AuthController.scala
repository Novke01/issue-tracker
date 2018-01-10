package com.issuetracker.controller

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import com.issuetracker.dto.LoginUser
import com.issuetracker.dto.RefreshToken
import com.issuetracker.service.AuthService
import com.issuetracker.util.JwtUtil

import play.api.Logger
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.ControllerComponents

class AuthController(
  val cc: ControllerComponents, 
  val jwtUtil: JwtUtil, 
  val authService: AuthService
)(implicit val executionContext: ExecutionContext) 
  extends AbstractController(cc) {
  
  val logger: Logger = Logger(this.getClass())
  
  private val header = "Authorization"
  
  def login: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalUser = request.body.validate[LoginUser]
    optionalUser match {
      case JsSuccess(loginUser: LoginUser, _) =>
        authService.login(loginUser).map { result =>
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
  
  def refresh: Action[JsValue] = Action.async(parse.json) { request => 
    val optionToken = request.body.validate[RefreshToken]
    val id: Long = jwtUtil.decode(request) map { _.id } getOrElse { -1 }
    optionToken match {
      case JsSuccess(refreshToken: RefreshToken, _) =>
        authService.refresh(id, refreshToken.token).map { loggedInUser =>
          Ok(Json.toJson(loggedInUser))
        } recover {
          case _ =>
            BadRequest("Invalid refresh token.")
        }
      case JsError(_) => Future {
        BadRequest("Invalid data format.")
      }
    }
  }
  
}