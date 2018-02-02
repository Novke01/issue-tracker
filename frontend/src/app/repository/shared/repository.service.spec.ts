import { HttpRequest } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { Issue } from '../../issue/shared/issue.model';
import { RepositoryService } from '../shared/repository.service';
import { User } from './../../core/auth/user.model';
import { RepositorySave } from './repository-save.model';
import { Repository } from './repository.model';

describe("RepositoryService", () => {
  let service: RepositoryService;
  let httpMock: HttpTestingController;
  const repositoryUrl = "api/repositories";

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [RepositoryService]
      });
      service = TestBed.get(RepositoryService);
      httpMock = TestBed.get(HttpTestingController);
    })
  );

  it("should create", () => {
    expect(service).toBeTruthy();
  });

  it("should be able to get owned repositories", () => {
    const repository = new Repository();
    repository.id = 1;
    repository.name = "repo1";
    repository.url = "https://github.com/user/repo1";
    repository.description = "description";
    repository.ownerId = 1;

    const responseOwnedRepositories = [repository];

    service.getOwnedRepositories().subscribe(repositories => {
      expect(repositories).toEqual(responseOwnedRepositories);
    });

    const url = `${environment.baseUrl}${repositoryUrl}/owned`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === "GET";
      }, "GET to api/repositories/owned")
      .flush(responseOwnedRepositories, { status: 200, statusText: "OK" });

    httpMock.verify();
  });

  it("should be able to get contributed repositories", () => {
    const repository = new Repository();
    repository.id = 1;
    repository.name = "repo1";
    repository.url = "https://github.com/user/repo1";
    repository.description = "description";
    repository.ownerId = 1;

    const responseContributedRepositories = [repository];

    service.getContributedRepositories().subscribe(repositories => {
      expect(repositories).toEqual(responseContributedRepositories);
    });

    const url = `${environment.baseUrl}${repositoryUrl}/contributed`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === "GET";
      }, "GET to api/repositories/contributed")
      .flush(responseContributedRepositories, {
        status: 200,
        statusText: "OK"
      });

    httpMock.verify();
  });

  it("should be able to save new repo", () => {
    const dummyRepo: RepositorySave = {
      name: "repo name",
      url: "repo url",
      description: "repo description",
      ownerId: 1,
      contributors: [2]
    };

    const responseRepo: Repository = {
      id: 1,
      name: dummyRepo.name,
      url: dummyRepo.url,
      description: dummyRepo.description,
      ownerId: dummyRepo.ownerId
    };

    service.saveRepository(dummyRepo).subscribe(repository => {
      expect(repository).toBe(responseRepo);
    });

    const url = `${environment.baseUrl}${repositoryUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: RepositorySave = req.body;
        return req.url === url && req.method === "POST" && body === dummyRepo;
      }, "POST to api/repositories with repository data in json format")
      .flush(responseRepo, { status: 201, statusText: "Created" });

    httpMock.verify();
  });

  it("should be able to handle rejection from server", () => {
    const dummyRepo: RepositorySave = {
      name: "repo name",
      url: "repo url",
      description: "repo description",
      ownerId: 1,
      contributors: [2]
    };

    const responseRepo: Repository = {
      id: 1,
      name: dummyRepo.name,
      url: dummyRepo.url,
      description: dummyRepo.description,
      ownerId: dummyRepo.ownerId
    };

    service.saveRepository(dummyRepo).subscribe(
      repo => {
        expect(repo).toBeFalsy();
      },
      err => {
        expect(err).toBeTruthy();
      }
    );

    const url = `${environment.baseUrl}${repositoryUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: RepositorySave = req.body;
        return req.url === url && req.method === "POST" && body === dummyRepo;
      }, "POST to api/repositories with repository data in json format")
      .flush(null, { status: 400, statusText: "BadRequest" });

    httpMock.verify();
  });

  it("should be able to get repository by id", () => {
    const id = 1;
    const responseRepo: Repository = {
      id: id,
      name: "repo name",
      url: "repo url",
      description: "repo description",
      ownerId: 1
    };

    service.getRepositoryById(id.toString()).subscribe(repository => {
      expect(repository).toBe(responseRepo);
    });

    const url = `${environment.baseUrl}${repositoryUrl}/${id}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === "GET";
      }, "GET to api/repositories/{id} with repository id")
      .flush(responseRepo, { status: 200, statusText: "OK" });

    httpMock.verify();
  });

  it("should be able to get repository contributors by repository id", () => {
    const repoId = 1;
    const contributor: User = {
      id: 1,
      username: "username",
      firstName: "first name",
      lastName: "last name",
      email: "email@email.com",
      exp: 111111
    };
    const responseContributors = [contributor];

    service
      .getContributorsByRepositoryId(repoId.toString())
      .subscribe(contributors => {
        expect(contributors).toEqual(responseContributors);
      });

    const url = `${environment.baseUrl}${repositoryUrl}/${repoId}/contributors`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === "GET";
      }, "GET to api/repositories/{id}/contributors with repository id")
      .flush(responseContributors, { status: 200, statusText: "OK" });

    httpMock.verify();
  });

  it("should be able to get repository owner by repository id", () => {
    const repoId = 1;
    const responseOwner: User = {
      id: 1,
      username: "username",
      firstName: "first name",
      lastName: "last name",
      email: "email@email.com",
      exp: 111111
    };

    service.getOwnerByRepositoryId(repoId.toString()).subscribe(owner => {
      expect(owner).toEqual(responseOwner);
    });

    const url = `${environment.baseUrl}${repositoryUrl}/${repoId}/owner`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === "GET";
      }, "GET to api/repositories/{id}/owner with repository id")
      .flush(responseOwner, { status: 200, statusText: "OK" });

    httpMock.verify();
  });

  it("should be able to get owner and contributors by search term", () => {
    const repoId = 1;
    const responseUser: User = {
      id: 1,
      username: "username",
      firstName: "first name",
      lastName: "last name",
      email: "email@email.com",
      exp: 111111
    };

    const term = "username";

    const responseUsers = [responseUser];

    service.searchOwnerAndContributors(repoId, term).subscribe(users => {
      expect(users).toEqual(responseUsers);
    });

    const url = `${
      environment.baseUrl
    }${repositoryUrl}/${repoId}/contributors/${term}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === "GET";
      }, "GET to api/repositories/{id}/contributors/{term} with repository id and search term")
      .flush(responseUsers, { status: 200, statusText: "OK" });

    httpMock.verify();
  });

  it("should be able to get issues by repository id", () => {
    const repoId = 1;
    const responseIssue: Issue = {
      id: 1,
      repositoryId: 1,
      title: "issue title",
      description: "issue description",
      created: 11111,
      ownerId: 1,
      status: "OPENED",
      assignees: [1, 2]
    };

    const term = "username";

    const responseIssues = [responseIssue];

    service.getIssuesByRepositoryId(repoId).subscribe(issues => {
      expect(issues).toEqual(responseIssues);
    });

    const url = `${environment.baseUrl}${repositoryUrl}/${repoId}/issues`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === "GET";
      }, "GET to api/repositories/{id}/issues with repository id")
      .flush(responseIssues, { status: 200, statusText: "OK" });

    httpMock.verify();
  });
});
