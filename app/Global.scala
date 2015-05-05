import scala.io.Source

import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import play.api.mvc.WithFilters
import play.filters.gzip.GzipFilter

object Global extends WithFilters(new GzipFilter()) with GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    try {
      system.programs.whitelist = Source.fromFile("whitelist.txt").getLines.toSet
    } catch {
      case t: Throwable => ;
    }
    
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}