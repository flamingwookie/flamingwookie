package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def greet = Action {
    val helloWorld = Map("longDescription" -> "Hello, World!")
    Ok(Json.toJson(helloWorld))
  }
  
}