package controllers

import java.io.File
import scala.sys.process.stringToProcess
import play.api.libs.json.JsError
import play.api.libs.json.JsString
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.BodyParsers
import play.api.mvc.Controller
import system.programs
import system.programs.getInstalledPrograms
import system.programs.getManpage
import java.nio.file.Paths
import java.nio.file.Files
import java.nio.charset.StandardCharsets

object Application extends Controller {

  def index = Action { request =>

    getInstalledPrograms match {
      case Some(s) => Ok(views.html.index("Thandril", s, programs.whitelist.toList.sorted))
      case _ => InternalServerError
    }

  }

  def programInfo(program: String) = Action {
    val man = getManpage(program)
    val response = Json.obj("name" -> JsString(program), "description" -> JsString(man))
    Ok(response)
  }

  def upload = Action(parse.multipartFormData) { request =>
    val directory = new File("./uploadedPrograms");

    if (!(directory exists))
      directory.mkdir()

    request.body.file("file").map { file =>
      import java.io.File
      val filename = file.filename
      val contentType = file.contentType
      file.ref.moveTo(new File(s"./uploadedPrograms/$filename"))
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file")
    }
  }

  def getWhitelist = Action {
    Ok(Json.toJson(programs.whitelist))
  }

  def pushWhitelist = Action(BodyParsers.parse.json) { request =>
    val whitelist = request.body.validate[Set[String]]
    whitelist.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      set => {
        programs.whitelist = set
        Files.write(Paths.get("whitelist.txt"), set.mkString("\n").getBytes(StandardCharsets.UTF_8))
        Ok(Json.obj())
      })
  }
}