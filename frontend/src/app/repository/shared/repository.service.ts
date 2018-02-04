import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError } from 'rxjs/operators';

import { environment } from '../../../environments/environment';
import { User } from '../../core/auth/user.model';
import { Issue } from '../../issue/shared/issue.model';
import { RepositorySave } from './repository-save.model';
import { Repository } from './repository.model';

@Injectable()
export class RepositoryService {
  private repositoryUrl = "api/repositories";

  constructor(private http: HttpClient) {}

  getOwnedRepositories(): Observable<Repository[]> {
    const url = `${environment.baseUrl}${this.repositoryUrl}/owned`;
    return this.http.get<Repository[]>(url).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  getContributedRepositories(): Observable<Repository[]> {
    const url = `${environment.baseUrl}${this.repositoryUrl}/contributed`;
    return this.http.get<Repository[]>(url).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  updateRepository(repository: RepositorySave): Observable<Repository> {
        const url = `${environment.baseUrl}${this.repositoryUrl}`;
        return this.http.patch<Repository>(url, repository);
  }

  saveRepository(repository: RepositorySave): Observable<Repository> {
    const url = `${environment.baseUrl}${this.repositoryUrl}`;
    return this.http.post<Repository>(url, repository).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  getRepositoryById(id: string): Observable<Repository> {
    const url = `${environment.baseUrl}${this.repositoryUrl}/${id}`;
    return this.http.get<Repository>(url).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  getContributorsByRepositoryId(id: string): Observable<User[]> {
    const url = `${environment.baseUrl}${
      this.repositoryUrl
    }/${id}/contributors`;
    return this.http.get<User[]>(url).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  getOwnerByRepositoryId(id: string): Observable<User> {
    const url = `${environment.baseUrl}${this.repositoryUrl}/${id}/owner`;
    return this.http.get<User>(url).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  /* GET contributors and owner whose name contains search term */
  searchOwnerAndContributors(
    repositoryId: number,
    term: string
  ): Observable<User[]> {
    if (!term.trim()) {
      // if not search term, return empty users array.
      return of([]);
    }
    const url = `${environment.baseUrl}${
      this.repositoryUrl
    }/${repositoryId}/contributors/${term}`;
    return this.http.get<User[]>(url).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  getIssuesByRepositoryId(repoId: number): Observable<Issue[]> {
    const url = `${environment.baseUrl}${this.repositoryUrl}/${repoId}/issues`;
    return this.http.get<Issue[]>(url).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }
}
