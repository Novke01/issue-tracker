package com.issuetracker.service

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import com.github.t3hnar.bcrypt.Password
import com.issuetracker.dto.LoggedInUser
import com.issuetracker.dto.LoginUser
import com.issuetracker.exception.IncorrectPasswordException
import com.issuetracker.exception.InvalidRefreshTokenException
import com.issuetracker.exception.UserNotFoundException
import com.issuetracker.model.User
import com.issuetracker.repository.UserRepository
import com.issuetracker.util.JwtUtil

import play.api.Logger

class AuthService(
  val userRepository: UserRepository, 
  val jwtUtil: JwtUtil
)(implicit ec: ExecutionContext) {
  
  private val logger = Logger(this.getClass())
  
  def login(loginUser: LoginUser): Future[LoggedInUser] = {
    userRepository.findByUsername(loginUser.username).map { result => 
      result match {
        case Some(user: User) => 
          if (loginUser.password.isBcrypted(user.password)) {
            logger.info("user successfully logged in")
            LoggedInUser(jwtUtil.generate(user), user.refreshToken)
          } else {
            logger.error("user not logged in")
            throw new IncorrectPasswordException
          }
        case None => 
          logger.error("user not found")
          throw new UserNotFoundException(s"User with username ${loginUser.username} doesn't exist.")
      }
    }
  }
  
  def refresh(id: Long, refreshToken: String): Future[LoggedInUser] = {
    userRepository.findById(id).map { userOption =>
      userOption match {
        case Some(user) =>
          if (user.refreshToken == refreshToken) {
            logger.info("user successfully refreshed his token")
            LoggedInUser(jwtUtil.generate(user), user.refreshToken)
          } else {
            logger.error("user sent invalid refresh token")
            throw new InvalidRefreshTokenException
          }
        case None =>
          logger.error("user not found")
          throw new UserNotFoundException
      }
    }
  }
  
}

object AuthService {
  
  def apply(
    userRepository: UserRepository, 
    jwtUtil: JwtUtil
  )(implicit ec: ExecutionContext): AuthService =
    new AuthService(userRepository, jwtUtil)
  
}