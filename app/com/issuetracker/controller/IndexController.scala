package com.issuetracker.controller

import play.api.mvc._

class IndexController(val cc: ControllerComponents) extends AbstractController(cc) {
  
  def index = Action {
    Ok("Hello World!")
  }
  
}