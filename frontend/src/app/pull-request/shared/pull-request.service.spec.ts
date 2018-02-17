import { HttpRequest } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { PullRequest } from './pull-request.model';
import { PullRequestService } from './pull-request.service';

describe('PullRequestService', () => {
  let service: PullRequestService;
  let httpMock: HttpTestingController;
  const pullRequestUrl = 'api/pull-requests';

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [PullRequestService]
      });
      service = TestBed.get(PullRequestService);
      httpMock = TestBed.get(HttpTestingController);
    })
  );

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('should be able to get pull requests by repository id', () => {
    const repositoryId = 1;
    const pullRequest = new PullRequest();

    pullRequest.id = 1;
    pullRequest.title = 'PR title';
    pullRequest.url = 'PR url';
    pullRequest.repositoryId = repositoryId;

    const responsePullRequests = [pullRequest];

    service.getByRepositoryId(repositoryId).subscribe(pullRequests => {
      expect(pullRequests).toEqual(responsePullRequests);
    });

    const url = `${
      environment.baseUrl
    }${pullRequestUrl}/repository/${repositoryId}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === 'GET';
      }, `GET to api/pull-requests/repository/${repositoryId}`)
      .flush(responsePullRequests, { status: 200, statusText: 'OK' });

    httpMock.verify();
  });

  it('should be able to save new pull request', () => {
    const dummyPullRequest: PullRequest = {
      id: 1,
      title: 'PR title',
      url: 'PR url',
      repositoryId: 1
    };

    const responsePullRequest: PullRequest = {
      id: dummyPullRequest.id,
      title: dummyPullRequest.title,
      url: dummyPullRequest.url,
      repositoryId: dummyPullRequest.repositoryId
    };

    service.createPullRequest(dummyPullRequest).subscribe(pullRequest => {
      expect(pullRequest).toBe(responsePullRequest);
    });

    const url = `${environment.baseUrl}${pullRequestUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: PullRequest = req.body;
        return (
          req.url === url && req.method === 'POST' && body === dummyPullRequest
        );
      }, 'POST to api/pull-requests with pull request data in json format')
      .flush(responsePullRequest, { status: 201, statusText: 'Created' });

    httpMock.verify();
  });

  it('should be able to handle rejection from server', () => {
    const dummyPullRequest: PullRequest = {
      id: 1,
      title: 'PR title',
      url: 'PR url',
      repositoryId: 1
    };

    const responsePullRequest: PullRequest = {
      id: dummyPullRequest.id,
      title: dummyPullRequest.title,
      url: dummyPullRequest.url,
      repositoryId: dummyPullRequest.repositoryId
    };

    service.createPullRequest(dummyPullRequest).subscribe(
      repo => {
        expect(repo).toBeFalsy();
      },
      err => {
        expect(err).toBeTruthy();
      }
    );

    const url = `${environment.baseUrl}${pullRequestUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: PullRequest = req.body;
        return (
          req.url === url && req.method === 'POST' && body === dummyPullRequest
        );
      }, 'POST to api/pull-requests with pull request data in json format')
      .flush(null, { status: 400, statusText: 'BadRequest' });

    httpMock.verify();
  });
});
