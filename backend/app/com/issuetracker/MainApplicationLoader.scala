package com.issuetracker

import scala.collection.Seq
import com.issuetracker.controller._
import com.issuetracker.filter.JwtFilter
import com.issuetracker.repository._
import com.issuetracker.service._
import com.issuetracker.util.JwtUtil
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import play.api.ApplicationLoader.Context
import play.api.db.slick.DbName
import play.api.db.slick.SlickComponents
import play.api.mvc.EssentialFilter
import play.filters.HttpFiltersComponents
import play.filters.cors.CORSComponents
import router.Routes
import slick.jdbc.JdbcProfile
import scala.concurrent.duration.Duration.Inf

import scala.concurrent.Await

class MainApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with CORSComponents
    with SlickComponents {

  lazy val dbConfig = slickApi.dbConfig[JdbcProfile](DbName("default"))

  lazy val userRepository         = new UserRepository(dbConfig.db)
  lazy val repositoryRepository   = RepositoryRepository(dbConfig.db)
  lazy val contributorRepository  = ContributorRepository(dbConfig.db)
  lazy val milestoneRepository    = new MilestoneRepository(dbConfig.db)
  lazy val issueRepository        = new IssueRepository(dbConfig.db)
  lazy val assignedUserRepository = new AssignedUserRepository(dbConfig.db)
  lazy val wikiPageRepository     = new WikiPageRepository(dbConfig.db)
  lazy val labelRepository        = new LabelRepository(dbConfig.db)
  lazy val issueLabelRepository   = new IssueLabelRepository(dbConfig.db)
  lazy val pullRequestRepository  = new PullRequestRepository(dbConfig.db)
  lazy val commentRepository      = new CommentRepository(dbConfig.db)

  lazy val jwtUtil = JwtUtil(configuration)

  Await.result(assignedUserRepository.drop(), Inf)
  Await.result(commentRepository.drop(), Inf)
  Await.result(issueLabelRepository.drop(), Inf)
  Await.result(labelRepository.drop(), Inf)
  Await.result(wikiPageRepository.drop(), Inf)
  Await.result(issueRepository.drop(), Inf)
  Await.result(pullRequestRepository.drop(), Inf)
  Await.result(milestoneRepository.drop(), Inf)
  Await.result(contributorRepository.drop(), Inf)
  Await.result(repositoryRepository.drop(), Inf)
  Await.result(userRepository.drop(), Inf)

  Await.result(userRepository.create(), Inf)
  Await.result(repositoryRepository.create(), Inf)
  Await.result(contributorRepository.create(), Inf)
  Await.result(milestoneRepository.create(), Inf)
  Await.result(pullRequestRepository.create(), Inf)
  Await.result(issueRepository.create(), Inf)
  Await.result(wikiPageRepository.create(), Inf)
  Await.result(labelRepository.create(), Inf)
  Await.result(issueLabelRepository.create(), Inf)
  Await.result(commentRepository.create(), Inf)
  Await.result(assignedUserRepository.create(), Inf)

  lazy val userService        = UserService(userRepository)
  lazy val authService        = AuthService(userRepository, jwtUtil)
  lazy val contributorService = new ContributorService(contributorRepository)
  lazy val repositoryService  = RepositoryService(repositoryRepository, contributorService)
  lazy val issueService =
    new IssueService(issueRepository, issueLabelRepository, assignedUserRepository)
  lazy val assignedUserService = new AssignedUserService(assignedUserRepository, issueRepository)
  lazy val wikiPageService     = new WikiPageService(wikiPageRepository)
  lazy val labelService        = new LabelService(labelRepository)
  lazy val issueLabelService   = new IssueLabelService(issueLabelRepository, issueRepository)
  lazy val milestoneService    = new MilestoneService(milestoneRepository)
  lazy val pullRequestService  = new PullRequestService(pullRequestRepository)
  lazy val commentService      = new CommentService(commentRepository)

  lazy val userController = new UserController(controllerComponents, userService)
  lazy val authController = new AuthController(controllerComponents, jwtUtil, authService)
  lazy val repositoryController = new RepositoryController(controllerComponents,
                                                           repositoryService,
                                                           contributorService,
                                                           issueService,
                                                           labelService,
                                                           wikiPageService,
                                                           jwtUtil)
  lazy val issueController =
    new IssueController(controllerComponents,
                        issueService,
                        issueLabelService,
                        assignedUserService,
                        jwtUtil)
  lazy val wikiPageController  = new WikiPageController(controllerComponents, wikiPageService)
  lazy val labelController     = new LabelController(controllerComponents, labelService)
  lazy val milestoneController = new MilestoneController(controllerComponents, milestoneService)
  lazy val pullRequestController =
    new PullRequestController(controllerComponents, pullRequestService)
  lazy val commentController = new CommentController(controllerComponents, commentService)

  lazy val authRouter        = new auth.Routes(httpErrorHandler, authController)
  lazy val userRouter        = new user.Routes(httpErrorHandler, userController)
  lazy val repoRoutes        = new repo.Routes(httpErrorHandler, repositoryController)
  lazy val issueRouter       = new issue.Routes(httpErrorHandler, issueController)
  lazy val wikipageRouter    = new wikipage.Routes(httpErrorHandler, wikiPageController)
  lazy val labelRouter       = new label.Routes(httpErrorHandler, labelController)
  lazy val milestoneRouter   = new milestone.Routes(httpErrorHandler, milestoneController)
  lazy val pullRequestRouter = new pullrequest.Routes(httpErrorHandler, pullRequestController)
  lazy val commentRouter     = new comment.Routes(httpErrorHandler, commentController)

  lazy val router =
    new Routes(httpErrorHandler,
               authRouter,
               userRouter,
               repoRoutes,
               issueRouter,
               wikipageRouter,
               labelRouter,
               milestoneRouter,
               pullRequestRouter,
               commentRouter)

  lazy val jwtFilter = JwtFilter(jwtUtil)

  override lazy val httpFilters: Seq[EssentialFilter] = Seq(corsFilter, jwtFilter)

}
