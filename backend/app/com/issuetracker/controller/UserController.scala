package com.issuetracker.controller

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.{ExecutionContext, Future}
import com.issuetracker.repository.UserRepository
import com.issuetracker.model.User
import com.issuetracker.dto.RegisterUser
import com.issuetracker.service.UserService
import org.postgresql.util.PSQLException

class UserController(val cc: ControllerComponents, val userService: UserService)
  (implicit val executionContext: ExecutionContext) 
  extends AbstractController(cc) {

  def register: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalUser = request.body.validate[RegisterUser]
    optionalUser match {
      case JsSuccess(registerUser: RegisterUser, path: JsPath) => 
        userService.register(registerUser).map { result => 
          Created(Json.toJson(result)) 
        } recover {
          case _ =>
            BadRequest("Something went wrong.")
        }
      case err: JsError => Future {
        BadRequest
      }
    }
  }
  
}