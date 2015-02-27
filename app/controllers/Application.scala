package controllers

import play.api._
import play.api.mvc._
import system.programs._
import play.api.libs.json.JsString
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper

object Application extends Controller {

  def index = Action { request =>

    getInstalledPrograms match {
      case Some(s) => Ok(views.html.index("Thandril", s))
      case _ => InternalServerError
    }

  }

  def programInfo(program: String) = Action {
    val man = getManpage(program)
    val response = Json.obj("name" -> JsString(program), "description" -> JsString(man))
    Ok(response)
  }

  def upload = Action(parse.temporaryFile) { request =>
    import java.io.File
    
    val directory = new File("./uploadedPrograms");
    
    if (!(directory exists))
      directory.mkdir()
      println(request.body)
    request.body.moveTo(new File("./uploadedPrograms/xyz"), true)
    Ok("File uploaded")
  }

}