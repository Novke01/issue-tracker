package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.Milestone
import org.joda.time.DateTime
import play.api.libs.json.JodaWrites

case class GetMilestone(
    id: Long,
    title: String,
    description: String,
    dueDate: DateTime,
    repositoryId: Long
)

object GetMilestone {

  implicit val dateTimeWriter: Writes[DateTime] = JodaWrites.jodaDateWrites("dd/MM/yyyy")

  implicit val getMilestoneWrites: OWrites[GetMilestone] = Json.writes[GetMilestone]

  implicit def milestoneToGetMilestone(milestone: Milestone): GetMilestone =
    new GetMilestone(
      milestone.id,
      milestone.title,
      milestone.description,
      milestone.dueDate,
      milestone.repositoryId
    )

}
