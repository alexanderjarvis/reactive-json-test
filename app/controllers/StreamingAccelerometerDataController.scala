package controllers

import play.api._
import play.api.mvc._

import play.api.libs.iteratee._
import play.api.libs.json._

import domain.AccelerometerData
import dtos.AccelerometerDataDTO

/**
 *  Created by Alex Jarvis on 23/01/2014.
 */
object StreamingAccelerometerDataController extends Controller with StreamingController {

  type Model = AccelerometerData

  def parseJson(json: JsObject): Option[AccelerometerData] = {
    json.validate[AccelerometerDataDTO].fold(
      errors => {
        Logger.error("Invalid json")
        None
      },
      item => Some(AccelerometerData(item))
    )
  }

  // Our action that uses the body parser
  def insert = Action(bodyParser) { request =>
    val json = Json.obj(
      "importId" -> request.body.id,
      "errors" -> request.body.errors.mkString
    )
    Created(json).as(JSON)
  }

}