import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';

import { environment } from '../../../environments/environment';
import { PullRequest } from './pull-request.model';

@Injectable()
export class PullRequestService {
  private pullRequestUrl = 'api/pull-requests';

  constructor(private http: HttpClient) {}

  get(id: string): Observable<PullRequest> {
    const url = `${environment.baseUrl}${this.pullRequestUrl}/${id}`;
    return this.http.get<PullRequest>(url);
  }

  getByRepositoryId(id: number): Observable<PullRequest[]> {
    const url = `${environment.baseUrl}${this.pullRequestUrl}/repository/${id}`;
    return this.http.get<PullRequest[]>(url);
  }

  createPullRequest(pullRequest: PullRequest): Observable<PullRequest> {
    const url = `${environment.baseUrl}${this.pullRequestUrl}`;
    return this.http.post<PullRequest>(url, pullRequest).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }
}
