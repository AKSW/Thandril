package controllers

import play.api._
import play.api.mvc._
import system.programs._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Thandril", getInstalledPrograms))
  }

}