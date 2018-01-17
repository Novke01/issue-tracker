package com.issuetracker.service

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Random

import com.github.t3hnar.bcrypt.Password
import com.issuetracker.dto.RegisteredUser
import com.issuetracker.model.User
import com.issuetracker.repository.UserRepository

import play.api.Logger

class UserService(val userRepository: UserRepository)(implicit val ec: ExecutionContext) {
  
  private val logger = Logger(this.getClass())
 
  def register(user: User): Future[RegisteredUser] = {
    val hashedUser = 
      user.copy(password = user.password.bcrypt, refreshToken = generateRefreshToken)
    userRepository.insert(hashedUser) map { result =>
      logger.info("user created")
      result
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