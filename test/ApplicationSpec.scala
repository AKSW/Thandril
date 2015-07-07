import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.JsArray
import play.api.libs.json.JsValue
import play.api.libs.json.JsString

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "response with NotFound on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "deliver the index page" in {
      val result = controllers.Application.index()(FakeRequest())

      status(result) must equalTo(OK)
      charset(result) must beSome("utf-8")
      contentType(result) must beSome("text/html")
      contentAsString(result) must contain("Thandril")
    }

    "deliver the Whitelist" in {
      val result = controllers.Application.getWhitelist()(FakeRequest())
      
      status(result) must equalTo(OK)
      charset(result) must beSome("utf-8")
      contentType(result) must beSome("application/json")
      contentAsJson(result) must beAnInstanceOf[JsArray]
      contentAsString(result) must startWith("""["""")
      contentAsString(result) must endWith(""""]""")
    }
    
    "deliver ManPage for program cat" in {
      val result = controllers.Application.programInfo("cat")(FakeRequest())
      
      status(result) must equalTo(OK)
      charset(result) must beSome("utf-8")
      contentType(result) must beSome("application/json")
      contentAsJson(result) \("name") must equalTo(JsString("cat"))
      
    }
  }
}
