package com.issuetracker.model

import slick.jdbc.PostgresProfile.api._
import com.issuetracker.util.EnumUtil

case object IssueStatus extends Enumeration {

  type IssueStatus = Value

  val OPENED = Value("OPENED")
  val CLOSED = Value("CLOSED")

  implicit val enumTypeFormat = EnumUtil.enumFormat(IssueStatus)

  implicit val statusMapper = MappedColumnType.base[IssueStatus, String](
    e => e.toString,
    s => IssueStatus.withName(s)
  )


}