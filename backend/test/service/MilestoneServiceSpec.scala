package service

import com.issuetracker.model.Milestone
import com.issuetracker.repository.MilestoneRepository
import com.issuetracker.service.MilestoneService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.issuetracker.dto.PostMilestone
import org.joda.time.DateTime
import org.postgresql.util.{PSQLException, PSQLState}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.Matchers._

class MilestoneServiceSpec extends PlaySpec with MockitoSugar {

  "MilestoneService#getByRepositoryId" should {
    "return all milestones that is defined in repository with given id" in {

      val repositoryId = 1
      val milestone = Milestone(
        1,
        "milestone title",
        "milestone description",
        new DateTime(),
        repositoryId
      )
      val mockMilestoneRepository = mock[MilestoneRepository]
      when(mockMilestoneRepository.getByRepositoryId(any[Int])) thenReturn Future { Seq(milestone) }
      val service = MilestoneService(mockMilestoneRepository)
      service.getByRepositoryId(repositoryId) map { milestones =>
        milestones.length mustBe 1
        val returnedMilestone = milestones.head
        returnedMilestone.id mustBe milestone.id
        returnedMilestone.title mustBe milestone.title
        returnedMilestone.description mustBe milestone.description
        returnedMilestone.dueDate mustBe milestone.dueDate
      }
    }
    "return empty sequence if the repository with given id doesn't have milestones" in {

      val repositoryId = 1

      val mockMilestoneRepository = mock[MilestoneRepository]
      when(mockMilestoneRepository.getByRepositoryId(any[Int])) thenReturn Future { Seq() }
      val service = MilestoneService(mockMilestoneRepository)
      service.getByRepositoryId(repositoryId) map { milestones =>
        milestones.length mustBe 0
      }
    }
  }

  "MilestoneService#get" should {
    "return milestone with given id" in {

      val repositoryId = 1
      val milestoneId = 1

      val milestone = Milestone(
        milestoneId,
        "milestone title",
        "milestone description",
        new DateTime(),
        repositoryId
      )
      val mockMilestoneRepository = mock[MilestoneRepository]
      when(mockMilestoneRepository.get(any[Int])) thenReturn Future { Some(milestone) }
      val service = MilestoneService(mockMilestoneRepository)
      service.get(milestoneId) map { returnedMilestone =>
        returnedMilestone.get.id mustBe milestone.id
        returnedMilestone.get.title mustBe milestone.title
        returnedMilestone.get.description mustBe milestone.description
        returnedMilestone.get.dueDate mustBe milestone.dueDate
      }
    }
    "return None if the milestone with given id doesn't exist" in {

      val milestoneId = 1

      val mockMilestoneRepository = mock[MilestoneRepository]
      when(mockMilestoneRepository.get(any[Int])) thenReturn Future { None }
      val service = MilestoneService(mockMilestoneRepository)
      service.get(milestoneId) map { milestone =>
        milestone mustBe None
      }
    }
  }

  "MilestoneService#save" should {
    "save milestone and return that milestone with id" in {

      val date = new DateTime()

      val milestone = PostMilestone(
        "milestone title",
        "milestone description",
        date,
        1
      )
      val createdMilestone = Milestone(
        1,
        milestone.title,
        milestone.description,
        milestone.dueDate,
        milestone.repositoryId
      )
      val mockMilestoneRepository = mock[MilestoneRepository]
      when(mockMilestoneRepository.insert(any[Milestone])) thenReturn Future { createdMilestone }
      val service = MilestoneService(mockMilestoneRepository)
      service.insert(milestone) map { returnedMilestone =>
        returnedMilestone.id mustBe createdMilestone.id
        returnedMilestone.title mustBe createdMilestone.title
        returnedMilestone.description mustBe createdMilestone.description
        returnedMilestone.dueDate mustBe createdMilestone.dueDate
      }
      }
    }
    "throw PSQLException when repository with given id doesn't exist" in {

      val milestone = PostMilestone(
        "milestone title",
        "milestone description",
        new DateTime(),
        1
      )
      val mockMilestoneRepository = mock[MilestoneRepository]
      when(mockMilestoneRepository.insert(any[Milestone])) thenReturn Future {
        throw new PSQLException("Foreign key constraint violated.", PSQLState.DATA_ERROR)
      }
      val service = MilestoneService(mockMilestoneRepository)
      ScalaFutures.whenReady(service.insert(milestone).failed) { e =>
        e shouldBe an[PSQLException]
      }
  }
}
