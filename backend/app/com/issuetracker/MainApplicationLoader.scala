package com.issuetracker

import com.issuetracker.controller.UserController
import com.issuetracker.repository.UserRepository
import com.issuetracker.service.UserService

import play.api.ApplicationLoader
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.db.slick.DbName
import play.api.db.slick.SlickComponents
import play.filters.HttpFiltersComponents
import router.Routes
import slick.jdbc.JdbcProfile
import controllers.AssetsComponents
import play.api.mvc.EssentialFilter
import play.filters.cors.CORSConfig
import com.issuetracker.filter.JwtFilter
import play.filters.cors.CORSComponents

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
  
  lazy val userService = new UserService(userRepository, configuration)
  
  lazy val userController = new UserController(controllerComponents, userService)
  
  lazy val router = new Routes(httpErrorHandler, userController)
  
  lazy val jwtFilter = JwtFilter(configuration)
  
  override lazy val httpFilters: Seq[EssentialFilter] = Seq(corsFilter, jwtFilter)
  
}