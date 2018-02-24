package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.WikiPage

case class PostWikiPage(id: Option[Long],
                        name: String,
                        content: String,
                        repositoryId: Long
                       )

object PostWikiPage {

  implicit val repositoryReads: Reads[PostWikiPage] = Json.reads[PostWikiPage]

  implicit def postWikiPageToWikiPage(postWikiPage: PostWikiPage): WikiPage =
    WikiPage(
      postWikiPage.id.getOrElse(-1),
      postWikiPage.name,
      postWikiPage.content,
      postWikiPage.repositoryId
    )

}
