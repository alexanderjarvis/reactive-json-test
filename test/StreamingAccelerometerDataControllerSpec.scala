package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import org.joda.time.DateTime

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import play.api.http.ContentTypes._
import play.api.libs.ws._

import scala.concurrent.duration._

import akka.util.Timeout

class StreamingAccelerometerDataControllerSpec extends Specification {

  val uuid = java.util.UUID.randomUUID().toString()
  val timestamp = new DateTime().getMillis()

  val repeatedJsonItem = Json.obj(
    "deviceId" -> uuid,
    "t" -> timestamp,
    "x" -> 0.027740478515625,
    "y" -> -0.0445556640625,
    "z" -> -1.002105712890625
  )

  val json = Json.obj(
    "importId" -> 1,
    "items" -> Array.fill(5000)(repeatedJsonItem)
  )

  "StreamingAccelerometerDataController" should {

    "insert with array of data" in new WithApplication {
      val result = route(FakeRequest(POST, "/streaming/accelerometerdata").withHeaders(CONTENT_TYPE -> JSON), json).get
      status(result)(Timeout(Duration(600, SECONDS))) must equalTo(CREATED)
    }

  }

}