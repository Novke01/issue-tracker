package com.issuetracker.repository.table

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.model.WikiPage
import slick.sql.SqlProfile.ColumnOption.SqlType

class WikiPageTable(tag: Tag) extends Table[WikiPage](tag, "wiki_pages") {

  def id           = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name         = column[String]("name")
  def content      = column[String]("content", SqlType.apply("TEXT"))
  def repositoryId = column[Long]("repositoryId")
  def repository   = foreignKey("repository_FK", repositoryId, WikiPageTable.wikiPages)(_.id)

  def * = (id, name, content, repositoryId) <> (WikiPage.tupled, WikiPage.unapply)

}

object WikiPageTable {

  lazy val wikiPages = TableQuery[WikiPageTable]

}
