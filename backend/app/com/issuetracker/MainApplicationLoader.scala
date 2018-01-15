package com.issuetracker

import scala.collection.Seq

import com.issuetracker.controller.AuthController
import com.issuetracker.controller.RepositoryController
import com.issuetracker.controller.UserController
import com.issuetracker.filter.JwtFilter
import com.issuetracker.repository.ContributorRepository
import com.issuetracker.repository.RepositoryRepository
import com.issuetracker.repository.UserRepository
import com.issuetracker.service.AuthService
import com.issuetracker.service.ContributorService
import com.issuetracker.service.RepositoryService
import com.issuetracker.service.UserService
import com.issuetracker.util.JwtUtil

import play.api.ApplicationLoader
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.db.slick.DbName
import play.api.db.slick.SlickComponents
import play.api.mvc.EssentialFilter
import play.filters.HttpFiltersComponents
import play.filters.cors.CORSComponents
import router.Routes
import slick.jdbc.JdbcProfile

class MainApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context) 
  extends BuiltInComponentsFromContext(context)
  with HttpFiltersComponents
  with CORSComponents
  with SlickComponents {
  
  lazy val dbConfig = slickApi.dbConfig[JdbcProfile](DbName("default"))
  
  lazy val userRepository = new UserRepository(dbConfig.db)
  lazy val repositoryRepository: RepositoryRepository = RepositoryRepository(dbConfig.db)
  lazy val contributorRepository = ContributorRepository(dbConfig.db)
  
  lazy val jwtUtil = JwtUtil(configuration)
  
  userRepository.create()
  repositoryRepository.create()
  contributorRepository.create()
  
  lazy val userService = UserService(userRepository)
  lazy val authService = AuthService(userRepository, jwtUtil)
  lazy val repositoryService = RepositoryService(repositoryRepository)
  lazy val contributorService = new ContributorService(contributorRepository, repositoryRepository)
  
  lazy val userController = new UserController(controllerComponents, userService)
  lazy val authController = new AuthController(controllerComponents, jwtUtil, authService)
  lazy val repositoryController = new RepositoryController(controllerComponents, repositoryService, contributorService, jwtUtil)

  lazy val authRouter = new auth.Routes(httpErrorHandler, authController)
  lazy val userRouter = new user.Routes(httpErrorHandler, userController)
  lazy val repoRoutes = new repo.Routes(httpErrorHandler, repositoryController)
  lazy val router = new Routes(httpErrorHandler, authRouter, userRouter, repoRoutes)
  
  lazy val jwtFilter = JwtFilter(jwtUtil)
  
  override lazy val httpFilters: Seq[EssentialFilter] = Seq(corsFilter, jwtFilter)
  
}