package system_tests

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import system.programs._
import play.api.test.WithApplication

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ProgramsSpec extends Specification {

  "Programs Class" should {

    "List all installed programs (example grep)" in new WithApplication() {
      getInstalledPrograms must beSome
      getInstalledPrograms.get.get("/usr/bin") must beSome
      val bla = getInstalledPrograms.get.get("/usr/bin").get.isEmpty must beFalse
    }

    "return the help text for a program (example: grep)" in {
      getParameters("grep") must contain("grep [OPTION]")
    }

    "return the Manpage for a program (example: cat)" in {
      getManpage("cat") must contain("cat")
    }

    "fail for non existent programs" in {
      val program = "aslkdnfsdohfal"
      getManpage(program) must equalTo("There is no manpage, I'm sorry")
      getParameters(program) must equalTo("There is no help text, I'm sorry")
      getInstalledPrograms.contains(program) must beFalse
    }
  }
}