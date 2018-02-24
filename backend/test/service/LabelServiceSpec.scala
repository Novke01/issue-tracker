package service

import com.issuetracker.dto.PostLabel
import com.issuetracker.model.Label
import com.issuetracker.repository.LabelRepository
import com.issuetracker.service.LabelService
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LabelServiceSpec extends PlaySpec with MockitoSugar {

  "LabelService#create" should {

    "return newLabel data for valid postLabel data" in {
      val postLabel = PostLabel(
        "newLabel",
        "#ff0000",
        1l
      )

      val newLabel = Label(
        1l,
        postLabel.name,
        postLabel.color,
        postLabel.repositoryId
      )

      val mockLabelRepository = mock[LabelRepository]
      when(mockLabelRepository.insert(any[Label])) thenReturn Future { newLabel }
      val service =
        LabelService(mockLabelRepository)
      service.insert(postLabel) map { returnedLabel =>
        returnedLabel.id mustBe newLabel.id
        returnedLabel.name mustBe newLabel.name
        returnedLabel.color mustBe newLabel.color
        returnedLabel.repositoryId mustBe newLabel.repositoryId
      }
    }
  }

  "LabelService#findById" should {
    "return label with given id" in {

      val labelId = 1

      val label = Label(
        1l,
        "newLabel",
        "#ff0000",
        1l
      )

      val mockLabelRepository = mock[LabelRepository]
      when(mockLabelRepository.findById(any[Int])) thenReturn Future {
        Some(label)
      }
      val service =
        LabelService(mockLabelRepository)
      service.findById(labelId) map { returnedLabel =>
        returnedLabel.get.id mustBe label.id
        returnedLabel.get.name mustBe label.name
        returnedLabel.get.color mustBe label.color
        returnedLabel.get.repositoryId mustBe label.repositoryId
      }
    }
    "return None if the label with given id doesn't exist" in {

      val labelId = 1

      val mockLabelRepository = mock[LabelRepository]
      when(mockLabelRepository.findById(any[Int])) thenReturn Future {
        None
      }
      val service =
        LabelService(mockLabelRepository)
      service.findById(labelId) map { issue =>
        issue mustBe None
      }
    }
  }

  "LabelService#findByRepositoryId" should {
    "return all labels for the repository with given id" in {

      val repositoryId = 1
      val label = Label(
        1l,
        "newLabel",
        "#ff0000",
        1l
      )
      val mockLabelRepository = mock[LabelRepository]
      when(mockLabelRepository.findByRepositoryId(any[Int])) thenReturn Future {
        Seq(label)
      }
      val service =
        LabelService(mockLabelRepository)
      service.findByRepositoryId(repositoryId) map { labels =>
        labels.length mustBe 1
        val returnedLabel = labels.head
        returnedLabel.id mustBe label.id
        returnedLabel.name mustBe label.name
        returnedLabel.color mustBe label.color
        returnedLabel.repositoryId mustBe label.repositoryId
      }
    }
    "return empty sequence if there is no label for the repository with given id" in {

      val repositoryId = 1

      val mockLabelRepository = mock[LabelRepository]
      when(mockLabelRepository.findByRepositoryId(any[Int])) thenReturn Future {
        Seq()
      }
      val service =
        LabelService(mockLabelRepository)
      service.findByRepositoryId(repositoryId) map { issues =>
        issues.length mustBe 0
      }
    }
  }

}
