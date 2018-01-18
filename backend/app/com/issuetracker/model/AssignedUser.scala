package com.issuetracker.model

case class AssignedUser(
                        id: Long,
                        userId: Long,
                        issueId: Long
                      )