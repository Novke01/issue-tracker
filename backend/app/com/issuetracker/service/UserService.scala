package com.issuetracker.service

import scala.concurrent.{ExecutionContext, Future}
import com.github.t3hnar.bcrypt._

import com.issuetracker.dto.{RegisterUser, RegisteredUser}
import com.issuetracker.repository.UserRepository

class UserService(val userRepository: UserRepository)
  (implicit val executionContext: ExecutionContext) {
 
  def register(user: RegisterUser): Future[RegisteredUser] = {
    val hashedUser = user.copy(password = user.password.bcrypt)
    userRepository.insert(hashedUser).map(result => result)
  }
  
}