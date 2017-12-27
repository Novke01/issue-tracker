package com.issuetracker;

import com.issuetracker.controller.IndexController

import play.api.ApplicationLoader
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.filters.HttpFiltersComponents
import router.Routes

class MainApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context) 
  extends BuiltInComponentsFromContext(context)
  with HttpFiltersComponents {
  
  lazy val indexController = new IndexController(controllerComponents)
  
  lazy val router = new Routes(httpErrorHandler, indexController)
}