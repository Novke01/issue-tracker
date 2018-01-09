package com.issuetracker.service

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import com.github.t3hnar.bcrypt.Password
import com.issuetracker.dto.JwtUser
import com.issuetracker.dto.LoggedInUser
import com.issuetracker.dto.LoginUser
import com.issuetracker.dto.RegisterUser
import com.issuetracker.dto.RegisteredUser
import com.issuetracker.exception.IncorrectPasswordException
import com.issuetracker.exception.UserNotFoundException
import com.issuetracker.model.User
import com.issuetracker.repository.UserRepository

import pdi.jwt.JwtAlgorithm
import pdi.jwt.JwtJson
import play.api.Logger
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import scala.util.Random
import play.api.Configuration
import com.issuetracker.exception.InvalidRefreshTokenException

class UserService(
  val userRepository: UserRepository, 
  val configuration: Configuration
)(implicit executionContext: ExecutionContext) {
  
  private val logger = Logger(this.getClass())
  private val secret = configuration.get[String]("jwt.secret")
 
  def register(user: User): Future[RegisteredUser] = {
    val hashedUser = 
      user.copy(password = user.password.bcrypt, refreshToken = generateRefreshToken)
    userRepository.insert(hashedUser).map(result => result)
  }
  
  def login(loginUser: LoginUser): Future[LoggedInUser] = {
    userRepository.findByUsername(loginUser.username).map { result => 
      result match {
        case Some(user: User) => 
          if (loginUser.password.isBcrypted(user.password)) {
            LoggedInUser(generateAccessToken(user), user.refreshToken)
          } else {
            logger.info("User not logged in.")
            throw new IncorrectPasswordException
          }
        case None => 
          logger.info("User not found.")
          throw new UserNotFoundException(s"User with username ${loginUser.username} doesn't exist.")
      }
    }
  }
  
  def refreshAccessToken(id: Long, refreshToken: String): Future[LoggedInUser] = {
    userRepository.findById(id).map { userOption =>
      userOption match {
        case Some(user) =>
          if (user.refreshToken == refreshToken) {
            LoggedInUser(generateAccessToken(user), user.refreshToken)
          } else {
            throw new InvalidRefreshTokenException
          }
        case None =>
          throw new UserNotFoundException
      }
    }
  }
  
  private def generateAccessToken(jwtUser: JwtUser): String = {
    val claim: JsObject = Json.toJsObject(jwtUser)
    val algo = JwtAlgorithm.HS256
    JwtJson.encode(claim, secret, algo)
  }
  
  private def generateRefreshToken(): String = {
    Random.alphanumeric.take(50).mkString
  }
  
}