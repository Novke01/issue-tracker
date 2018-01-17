import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../environments/environment';
import { Issue } from './issue.model';
import { of } from 'rxjs/observable/of';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable()
export class IssueService {

  private issuesUrl = 'api/issues';

  constructor(private http: HttpClient) { }

  createIssue (issue: Issue): Observable<Issue> {
    const url = `${environment.baseUrl}${this.issuesUrl}`;
    return this.http.post<Issue>(url, issue, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })});
  }

}
