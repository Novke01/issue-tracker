import { Repository } from './repository.model';
import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { catchError } from 'rxjs/operators';
import { Observable } from 'rxjs/Observable';
import { RepositorySave } from './repository-save.model';
import { User } from '../../core/auth/user.model';
import { of } from 'rxjs/observable/of';

@Injectable()
export class RepositoryService {

    private repositoryUrl = 'api/repositories';

    constructor(private http: HttpClient) { }

    getOwnedRepositories(): Observable<Repository[]> {
        const url = `${environment.baseUrl}${this.repositoryUrl}/owned`;
        return this.http.get<Repository[]>(url);
    }

    getContributedRepositories(): Observable<Repository[]> {
        const url = `${environment.baseUrl}${this.repositoryUrl}/contributed`;
        return this.http.get<Repository[]>(url);
    }

    saveRepository(repository: RepositorySave): Observable<Repository> {
        const url = `${environment.baseUrl}${this.repositoryUrl}`;
        return this.http.post<Repository>(url, repository);
    }

    getRepositoryById(id: string): Observable<Repository> {
        const url = `${environment.baseUrl}${this.repositoryUrl}/${id}`;
        return this.http.get<Repository>(url);
    }

    /* GET contributors and owner whose name contains search term */
    searchOwnerAndContributors(repositoryId: number, term: string): Observable<User[]> {
        if (!term.trim()) {
            // if not search term, return empty users array.
            return of([]);
        }
        const url = `${environment.baseUrl}${this.repositoryUrl}/${repositoryId}/contributors/${term}`;
        return this.http.get<User[]>(url);
    }

}
