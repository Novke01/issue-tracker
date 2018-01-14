import { Repository } from './repository.model';
import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { catchError } from 'rxjs/operators';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class RepositoryService {

    private repositoryUrl = 'api/repositories';

    constructor(private http: HttpClient) { }

    getOwnedRepositories(): Observable<Repository[]> {
        const url = `${environment.baseUrl}${this.repositoryUrl}`;
        return this.http.get<Repository[]>(url, {
            params: new HttpParams().set('isOwner', 'true'),
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }

    getContributedRepositories(): Observable<Repository[]> {
        const url = `${environment.baseUrl}${this.repositoryUrl}`;
        return this.http.get<Repository[]>(url, {
            params: new HttpParams().set('isContributor', 'true'),
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }
}
