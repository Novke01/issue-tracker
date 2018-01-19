package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.WikiPage

case class PostWikiPage(
    name: String,
    content: String,
    repositoryId: Long
)

object PostWikiPage {

  implicit val repositoryReads = Json.reads[PostWikiPage]

  implicit def postWikiPageToWikiPage(postWikiPage: PostWikiPage): WikiPage =
    new WikiPage(
      -1,
      postWikiPage.name,
      postWikiPage.content,
      postWikiPage.repositoryId
    )

}
