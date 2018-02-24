package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.Label

case class PostLabel(
    name: String,
    color: String,
    repositoryId: Long
)
object PostLabel {

  implicit val postLabelReads: Reads[PostLabel] = Json.reads[PostLabel]

  implicit def postLabelToLabel(postLabel: PostLabel): Label =
    Label(
      -1,
      postLabel.name,
      postLabel.color,
      postLabel.repositoryId
    )

}
