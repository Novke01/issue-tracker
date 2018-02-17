import { HttpRequest } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { Comment } from './comment.model';
import { CommentService } from './comment.service';

describe('PullRequestService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;
  const commentsUrl = 'api/comments';

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [CommentService]
      });
      service = TestBed.get(CommentService);
      httpMock = TestBed.get(HttpTestingController);
    })
  );

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('should be able to get all coments by issue id', () => {
    const issueId = 1;
    const comment = new Comment();

    comment.id = 1;
    comment.content = 'content';
    comment.userId = 1;
    comment.issueId = issueId;

    const responseComments = [comment];

    service.getById(issueId, 'issue').subscribe(comments => {
      expect(comments).toEqual(responseComments);
    });

    const url = `${environment.baseUrl}${commentsUrl}/issue/${issueId}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === 'GET';
      }, `GET to api/comments/issue/${issueId}`)
      .flush(responseComments, { status: 200, statusText: 'OK' });

    httpMock.verify();
  });

  it('should be able to get all coments by pull request id', () => {
    const pullRequestId = 1;
    const comment = new Comment();

    comment.id = 1;
    comment.content = 'content';
    comment.userId = 1;
    comment.pullRequestId = pullRequestId;

    const responseComments = [comment];

    service.getById(pullRequestId, 'pull-request').subscribe(comments => {
      expect(comments).toEqual(responseComments);
    });

    const url = `${
      environment.baseUrl
    }${commentsUrl}/pull-request/${pullRequestId}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === 'GET';
      }, `GET to api/comments/pull-request/${pullRequestId}`)
      .flush(responseComments, { status: 200, statusText: 'OK' });

    httpMock.verify();
  });

  it('should be able to save new comment', () => {
    const dummyComment = new Comment();
    dummyComment.id = 1;
    dummyComment.content = 'content';
    dummyComment.userId = 1;
    dummyComment.issueId = 1;

    const responseComment = new Comment();
    responseComment.id = 1;
    responseComment.content = 'content';
    responseComment.userId = 1;
    responseComment.issueId = 1;

    service.createComment(dummyComment).subscribe(comment => {
      expect(comment).toBe(responseComment);
    });

    const url = `${environment.baseUrl}${commentsUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: Comment = req.body;
        return (
          req.url === url && req.method === 'POST' && body === dummyComment
        );
      }, 'POST to api/comments with comment data in json format')
      .flush(responseComment, { status: 201, statusText: 'Created' });

    httpMock.verify();
  });

  it('should be able to handle rejection from server', () => {
    const dummyComment = new Comment();
    dummyComment.id = 1;
    dummyComment.content = 'content';
    dummyComment.userId = 1;
    dummyComment.issueId = 1;

    const responseComment = new Comment();
    responseComment.id = 1;
    responseComment.content = 'content';
    responseComment.userId = 1;
    responseComment.issueId = 1;

    service.createComment(dummyComment).subscribe(
      repo => {
        expect(repo).toBeFalsy();
      },
      err => {
        expect(err).toBeTruthy();
      }
    );

    const url = `${environment.baseUrl}${commentsUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: Comment = req.body;
        return (
          req.url === url && req.method === 'POST' && body === dummyComment
        );
      }, 'POST to api/comments with comment data in json format')
      .flush(null, { status: 400, statusText: 'BadRequest' });

    httpMock.verify();
  });
});
