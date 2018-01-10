package com.issuetracker.service

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Random

import com.github.t3hnar.bcrypt.Password
import com.issuetracker.dto.RegisteredUser
import com.issuetracker.model.User
import com.issuetracker.repository.UserRepository

class UserService(
  val userRepository: UserRepository
)(implicit executionContext: ExecutionContext) {
 
  def register(user: User): Future[RegisteredUser] = {
    val hashedUser = 
      user.copy(password = user.password.bcrypt, refreshToken = generateRefreshToken)
    userRepository.insert(hashedUser).map(result => result)
  }
  
  private def generateRefreshToken(): String = {
    Random.alphanumeric.take(50).mkString
  }
  
}