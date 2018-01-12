package com.issuetracker

import com.issuetracker.controller._
import com.issuetracker.filter.JwtFilter
import com.issuetracker.repository.{ContributorRepository, RepositoryRepository, UserRepository}
import com.issuetracker.service._
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
  userRepository.create()

  lazy val repositoryRepository = new RepositoryRepository(dbConfig.db, userRepository)
  repositoryRepository.create()

  lazy val contributorRepository = new ContributorRepository(dbConfig.db, userRepository, repositoryRepository)
  contributorRepository.create()
  
  lazy val jwtUtil = JwtUtil(configuration)
  
  lazy val userService = new UserService(userRepository)
  lazy val authService = new AuthService(userRepository, jwtUtil)
  lazy val repositoryService = new RepositoryService(repositoryRepository, contributorRepository)
  lazy val contributorService = new ContributorService(contributorRepository, repositoryRepository)
  
  lazy val userController = new UserController(controllerComponents, userService)
  lazy val authController = new AuthController(controllerComponents, jwtUtil, authService)
  lazy val repositoryController = new RepositoryController(controllerComponents, repositoryService, contributorService, jwtUtil)
  
  lazy val authRouter = new auth.Routes(httpErrorHandler, authController)
  lazy val userRouter = new user.Routes(httpErrorHandler, userController)
  lazy val repositoryRoutes = new repositoryroute.Routes(httpErrorHandler, repositoryController)
  lazy val router = new Routes(httpErrorHandler, authRouter, userRouter, repositoryRoutes)
  
  lazy val jwtFilter = JwtFilter(jwtUtil)
  
  override lazy val httpFilters: Seq[EssentialFilter] = Seq(corsFilter, jwtFilter)
  
}