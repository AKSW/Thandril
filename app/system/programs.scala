package system

import scala.sys.process._

object programs {

  /**
   * @todo find a way to print $path variabel
   */
  def getInstalledPrograms = {
    val dirContents = "ls /usr/bin".!! split ('\n')
    Map("System Programs" -> dirContents.tail)
  }

  def saveProgram = ???

  def getParameters(program: String) = {
    (program + " --help").!!
  }
  
  def getManpage(program: String) = {
    ("man "+program).!!
  }

}