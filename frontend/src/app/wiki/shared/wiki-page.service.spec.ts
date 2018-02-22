import { HttpRequest } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { WikiPageSave } from './wiki-page-save.model';
import { WikiPage } from './wiki-page.model';
import { WikiPageService } from './wiki-page.service';

describe('WikiPageService', () => {
  let service: WikiPageService;
  let httpMock: HttpTestingController;
  const wikiPageUrl = 'api/wiki-pages';
  const repositoryUrl = 'api/repositories';

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [WikiPageService]
      });
      service = TestBed.get(WikiPageService);
      httpMock = TestBed.get(HttpTestingController);
    })
  );

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('should be able to save new wiki page', () => {
    const dummyWikiPage = new WikiPageSave(null, 'name', 'content', 1);

    const responseWikiPage: WikiPage = {
      id: 1,
      name: dummyWikiPage.name,
      content: dummyWikiPage.content,
      repositoryId: dummyWikiPage.repositoryId
    };

    service.saveWikiPage(dummyWikiPage).subscribe(repository => {
      expect(repository).toBe(responseWikiPage);
    });

    const url = `${environment.baseUrl}${wikiPageUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: WikiPageSave = req.body;
        return (
          req.url === url && req.method === 'POST' && body === dummyWikiPage
        );
      }, 'POST to api/wiki-pages with wiki page data in json format')
      .flush(responseWikiPage, { status: 201, statusText: 'Created' });

    httpMock.verify();
  });

  it('should be able to handle rejection from server', () => {
    const dummyWikiPage = new WikiPageSave(null, 'name', 'content', 1);

    const responseWikiPage: WikiPage = {
      id: 1,
      name: dummyWikiPage.name,
      content: dummyWikiPage.content,
      repositoryId: dummyWikiPage.repositoryId
    };

    service.saveWikiPage(dummyWikiPage).subscribe(
      wiki => {
        expect(wiki).toBeFalsy();
      },
      err => {
        expect(err).toBeTruthy();
      }
    );

    const url = `${environment.baseUrl}${wikiPageUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: WikiPageSave = req.body;
        return (
          req.url === url && req.method === 'POST' && body === dummyWikiPage
        );
      }, 'POST to api/wiki-pages with wiki page data in json format')
      .flush(null, { status: 400, statusText: 'Bad Request' });

    httpMock.verify();
  });

  it('should be able to get repository by id', () => {
    const id = 1;
    const responseWikiPage: WikiPage = {
      id: id,
      name: 'wiki page name',
      content: 'wiki page content',
      repositoryId: 1
    };

    service.getWikiPageById(id.toString()).subscribe(wikiPage => {
      expect(wikiPage).toBe(responseWikiPage);
    });

    const url = `${environment.baseUrl}${wikiPageUrl}/${id}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === 'GET';
      }, 'GET to api/wiki-pages/{id} with wiki page id')
      .flush(responseWikiPage, { status: 200, statusText: 'OK' });

    httpMock.verify();
  });

  it('should be able to get wiki pages by repository id', () => {
    const repoId = 1;

    const responseWikiPage: WikiPage = {
      id: 1,
      name: 'wiki page name',
      content: 'wiki page content',
      repositoryId: repoId
    };
    const responseWikiPages = [responseWikiPage];

    service
      .getWikiPageByRepositoryId(repoId.toString())
      .subscribe(wikiPages => {
        expect(wikiPages).toEqual(responseWikiPages);
      });

    const url = `${environment.baseUrl}${repositoryUrl}/${repoId}/wiki-pages`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === 'GET';
      }, 'GET to api/repositories/{id}/wiki-pages with repository id')
      .flush(responseWikiPages, { status: 200, statusText: 'OK' });

    httpMock.verify();
  });
});
