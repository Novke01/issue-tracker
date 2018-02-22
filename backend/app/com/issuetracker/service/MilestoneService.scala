package com.issuetracker.service

import com.issuetracker.dto.{GetMilestone, PostMilestone}

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.issuetracker.repository.MilestoneRepository

class MilestoneService(
    val milestoneRepository: MilestoneRepository
)(implicit val ec: ExecutionContext) {

  def insert(milestone: PostMilestone): Future[GetMilestone] = {
    milestoneRepository.insert(milestone).map(GetMilestone.milestoneToGetMilestone)
  }

  def get(id: Long): Future[Option[GetMilestone]] = {
    milestoneRepository.get(id).map(_.map(GetMilestone.milestoneToGetMilestone))
  }

  def getByRepositoryId(id: Long): Future[Seq[GetMilestone]] = {
    milestoneRepository.getByRepositoryId(id).map(_.map(GetMilestone.milestoneToGetMilestone))
  }

  def delete(id: Long): Future[Int] = {
    milestoneRepository.delete(id)
  }
}

object MilestoneService {

  def apply(milestoneRepository: MilestoneRepository)(
      implicit ec: ExecutionContext): MilestoneService =
    new MilestoneService(milestoneRepository)

}
