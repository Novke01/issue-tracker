package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.Label

case class GetLabel(
    id: Long,
    name: String,
    color: String,
    repositoryId: Long
)

object GetLabel {

  implicit val getLabelWrites: OWrites[GetLabel] = Json.writes[GetLabel]

  implicit def labelToGetLabel(label: Label): GetLabel =
    new GetLabel(
      label.id,
      label.name,
      label.color,
      label.repositoryId
    )

}
