package system

import scala.sys.process.stringToProcess

import play.api.Logger

object programs {

  val whitelist = Set("cat", "grep", "dmesg", "sed")
  val dirs = List("/usr/bin", "./uploadedPrograms")

  /**
   * Selects all installed programs of the host machine, filtered by whitelist
   * @todo find a way to print $path variabel
   */
  def getInstalledPrograms = {

    val bla = dirs.map { dir =>
      try {
        val dirContents = ("ls " + dir).!!.split('\n')
        Some(Map(dir -> dirContents.filter { x => x != "" }.filter { x => if (dirs.head == dir) whitelist.contains(x) else true }))

      } catch {
        case t: Throwable => Logger.info("Directory not found", t); None
      }
    }
    bla.reduce((a, b) => a match {
      case Some(s1) => Some(b.getOrElse(Map()) ++ s1)
      case None => if (b.nonEmpty) b else None
    })
  }

  /**
   * Returns the help text of the passed program
   */
  def getParameters(program: String) = {
    try {
      (program + " --help").!!
    } catch {
      case t: Throwable => "There is no help text, I'm sorry"
    }
  }

  /**
   * Returns the manpage text of the passed program
   */
  def getManpage(program: String) = {
    try {
      ("man " + program).!!
    } catch {
      case t: Throwable => "There is no manpage, I'm sorry"
    }

  }

}