package com.issuetracker.dto

import play.api.libs.json._
import com.issuetracker.model.WikiPage

case class GetWikiPage(
    id: Long,
    name: String,
    content: String,
    repositoryId: Long
)

object GetWikiPage {

  implicit val wikiPageWrites: OWrites[GetWikiPage] = Json.writes[GetWikiPage]

  implicit def wikiPageToGetWikiPage(wikiPage: WikiPage): GetWikiPage =
    new GetWikiPage(
      wikiPage.id,
      wikiPage.name,
      wikiPage.content,
      wikiPage.repositoryId
    )

}
