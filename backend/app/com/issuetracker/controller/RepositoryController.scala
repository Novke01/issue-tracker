package com.issuetracker.controller

import com.issuetracker.dto.RefreshToken
import com.issuetracker.service.{ContributorService, RepositoryService}
import com.issuetracker.util.JwtUtil
import dto.{GetRepository, PostRepository}

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.Logger
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc._

class RepositoryController(val cc: ControllerComponents, val repositoryService: RepositoryService,
                           val contributorService: ContributorService,
                           val jwtUtil: JwtUtil)
                    (implicit val executionContext: ExecutionContext)
  extends AbstractController(cc) {

  val logger: Logger = Logger(this.getClass())

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalRepository = request.body.validate[PostRepository]
    Future {
      BadRequest
    }
    optionalRepository match {
      case JsSuccess(postRepository: PostRepository, _) =>
        contributorService.insert(postRepository).map { result =>
          Created(Json.toJson(result))
        } recover {
          case err =>
            logger.error(err.getMessage, err)
            BadRequest("Something went wrong.")
        }
      case _: JsError => Future {
        logger.error("Invalid repository data.")
        BadRequest("Invalid repository data.")
      }
    }
  }

  def all(isOwner: Boolean, isContributor: Boolean) = Action.async { request =>
    isOwner match {
      case true => {
        val id: Long = jwtUtil.decode(request) map { _.id } getOrElse { -1 }
        repositoryService.findByOwnerId(id).map { result =>
          Ok(Json.toJson(result))
        } recover {
          case err =>
            logger.error(err.getMessage, err)
            BadRequest("Something went wrong.")
        }
      }
      case false => {
        isContributor match {
          case true => {
            val id: Long = jwtUtil.decode(request) map { _.id } getOrElse { -1 }
            repositoryService.findByContributorId(id).map { result =>
              Ok(Json.toJson(result))
            } recover {
              case err =>
                logger.error(err.getMessage, err)
                BadRequest("Something went wrong.")
            }
          }
          case _ => Future {
            BadRequest("Wrong parameters")
          }

        }
      }
    }
  }
}