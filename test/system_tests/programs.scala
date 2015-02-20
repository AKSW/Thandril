package system_tests

import system.programs._
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class programs extends Specification {

  "Application" should {

    "List all installed programs" in new WithApplication {
      getInstalledPrograms.contains("grep") must beTrue
    }
    "Print help texts" in new WithApplication {
      getParameters("grep") must contain("grep [OPTION]")
    }
  }
}