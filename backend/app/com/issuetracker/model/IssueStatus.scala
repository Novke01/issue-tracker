package com.issuetracker.model

import com.issuetracker.model
import slick.jdbc.PostgresProfile.api._
import com.issuetracker.util.EnumUtils
import play.api.libs.json.Format
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType

case object IssueStatus extends Enumeration {

  type IssueStatus = Value

  val OPENED: model.IssueStatus.Value = Value("OPENED")
  val CLOSED: model.IssueStatus.Value = Value("CLOSED")

  implicit val enumTypeFormat: Format[model.IssueStatus.Value] = EnumUtils.enumFormat(IssueStatus)

  implicit val statusMapper: JdbcType[IssueStatus] with BaseTypedType[IssueStatus] =
    MappedColumnType.base[IssueStatus, String](
      e => e.toString,
      s => IssueStatus.withName(s)
    )

}
