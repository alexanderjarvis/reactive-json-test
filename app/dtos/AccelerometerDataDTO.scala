package dtos

import java.util.Date

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class AccelerometerDataDTO(
  deviceId: String,
  t: Date,
  x: Double,
  y: Double,
  z: Double
)

object AccelerometerDataDTO {
  implicit val format = Json.format[AccelerometerDataDTO]
}