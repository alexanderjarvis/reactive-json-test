package domain

import java.util.UUID
import java.util.Date

import dtos.AccelerometerDataDTO

case class AccelerometerData(
  deviceId: UUID,
  timestamp: Date,
  x: Double,
  y: Double,
  z: Double
)

object AccelerometerData {

  def apply(dto: AccelerometerDataDTO): AccelerometerData = {
    AccelerometerData(
      UUID.fromString(dto.deviceId),
      dto.t,
      dto.x,
      dto.y,
      dto.z
    )
  }

}
