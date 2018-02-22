package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.Milestone
import org.joda.time.DateTime

case class PostMilestone(
    title: String,
    description: String,
    dueDate: DateTime,
    repositoryId: Long
)

object PostMilestone {

  implicit val dateTimeReader: Reads[DateTime] = JodaReads.jodaDateReads("dd/MM/yyyy")

  implicit val postMilestoneReads: Reads[PostMilestone] = Json.reads[PostMilestone]

  implicit def GetMilestoneToMilestone(milestone: PostMilestone): Milestone =
    Milestone(
      -1,
      milestone.title,
      milestone.description,
      milestone.dueDate,
      milestone.repositoryId
    )

}
