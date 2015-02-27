package system

import scala.sys.process.stringToProcess

import play.api.Logger

object programs {

  val whitelist = Set("cat", "grep", "dmesg", "sed")
  val dirs = List("/usr/bin", "./bin")

  /**
   * Selects all installed programs of the host machine, filtered by whitelist
   * @todo find a way to print $path variabel
   */
  def getInstalledPrograms = {

    try {
      val dirContents = "ls /usr/bin".!! split ('\n')
      Some(Map("System Programs" -> dirContents.tail.filter { x => whitelist.contains(x) }))
    } catch {
      case t: Throwable => Logger.info("Directory not found", t); None
    }

  }

  def saveProgram = {
    if (!("ls").!!.contains("bin"))
      "mkdir bin".!!

    /*import java.io._

    val directory = new File("./bin");
    
    if (!(directory exists)) {
      if (directory mkdir) {
        //Successfully created new directory
      } else {
        //Failed to create new directory
      }
    }*/
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