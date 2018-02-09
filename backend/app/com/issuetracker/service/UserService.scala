package com.issuetracker.service

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Random

import com.github.t3hnar.bcrypt.Password
import com.issuetracker.exception.UserNotFoundException
import com.issuetracker.model.User
import com.issuetracker.repository.UserRepository

import play.api.Logger
import com.issuetracker.dto.RegisteredUser

class UserService(val userRepository: UserRepository)(implicit val ec: ExecutionContext) {

  private val logger = Logger(this.getClass())
 
  def register(user: User): Future[User] = {
    val hashedUser = 
      user.copy(password = user.password.bcrypt, refreshToken = generateRefreshToken)
    userRepository.insert(hashedUser) map { result =>
      logger.info("user created")
      result
    }
  }

  def getAll(): Future[Seq[RegisteredUser]] = {
    userRepository.all().map(_.map(RegisteredUser.userToRegisteredUser))
  }
  
  def get(id: Long): Future[User] = {
    userRepository.findById(id) map { optionUser =>
      optionUser getOrElse { throw new UserNotFoundException("User doesn't exist.") }
    }
  }
  
  private def generateRefreshToken(): String = {
    Random.alphanumeric.take(50).mkString
  }

}

object UserService {

  def apply(userRepository: UserRepository)(implicit ec: ExecutionContext): UserService =
    new UserService(userRepository)

}
