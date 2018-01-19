import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../environments/environment';
import { WikiPageSave } from './wiki-page-save.model';
import { WikiPage } from './wiki-page.model';

@Injectable()
export class WikiPageService {

    private wikiPageUrl = 'api/wiki-pages';
    private repositoryUrl = 'api/repositories';

    constructor(private http: HttpClient) { }

    saveWikiPage(wikiPage: WikiPageSave): Observable<WikiPage> {
        const url = `${environment.baseUrl}${this.wikiPageUrl}`;
        return this.http.post<WikiPage>(url, wikiPage);
    }

    getWikiPageById(id: string): Observable<WikiPage> {
        const url = `${environment.baseUrl}${this.wikiPageUrl}/${id}`;
        return this.http.get<WikiPage>(url);
    }

    getWikiPageByRepositoryId(repoId: string): Observable<WikiPage[]> {
        const url = `${environment.baseUrl}${this.repositoryUrl}/${repoId}/wiki-pages`;
        return this.http.get<WikiPage[]>(url);
    }

}
