package controllers

import play.api._
import play.api.mvc._
import system.programs._
import play.api.libs.json.JsString
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Thandril", getInstalledPrograms))
  }
  
  def programInfo(program: String) = Action {
    val man = getManpage(program)
    val response = Json.obj("name" -> JsString(program), "description" -> JsString(man))
    Ok(response)
  }

}