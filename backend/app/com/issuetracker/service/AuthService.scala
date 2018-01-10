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
)(implicit executionContext: ExecutionContext) {
  
  private val logger = Logger(this.getClass())
  
  def login(loginUser: LoginUser): Future[LoggedInUser] = {
    userRepository.findByUsername(loginUser.username).map { result => 
      result match {
        case Some(user: User) => 
          if (loginUser.password.isBcrypted(user.password)) {
            LoggedInUser(jwtUtil.generate(user), user.refreshToken)
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
  
  def refresh(id: Long, refreshToken: String): Future[LoggedInUser] = {
    userRepository.findById(id).map { userOption =>
      userOption match {
        case Some(user) =>
          if (user.refreshToken == refreshToken) {
            LoggedInUser(jwtUtil.generate(user), user.refreshToken)
          } else {
            throw new InvalidRefreshTokenException
          }
        case None =>
          throw new UserNotFoundException
      }
    }
  }
  
  
  
}