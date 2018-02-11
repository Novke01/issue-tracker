package com.issuetracker.service

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import com.github.t3hnar.bcrypt.Password
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

  private val logger = Logger(this.getClass)
  
  def login(username: String, password: String): Future[(String, String)] = {
    userRepository.findByUsername(username).map { result => 
      result map { user =>
        if (password.isBcrypted(user.password)) {
          logger.info("user successfully logged in")
          (jwtUtil.generate(user), user.refreshToken)
        } else {
          logger.error("user not logged in")
          throw new IncorrectPasswordException
        }
      } getOrElse {
        logger.error("user not found")
        throw new UserNotFoundException(s"User with username $username doesn't exist.")
      }
    }
  }
  
  def refresh(id: Long, refreshToken: String): Future[(String, String)] = {
    userRepository.findById(id).map { userOption =>
      userOption map { user =>
        if (user.refreshToken == refreshToken) {
          logger.info("user successfully refreshed his token")
          (jwtUtil.generate(user), user.refreshToken)
        } else {
          logger.error("user sent invalid refresh token")
          throw new InvalidRefreshTokenException
        }
      } getOrElse {
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
