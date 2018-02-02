import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../environments/environment';
import { User } from '../../core/auth/user.model';
import { Issue } from './issue.model';

@Injectable()
export class IssueService {
  private issuesUrl = "api/issues";

  constructor(private http: HttpClient) {}

  createIssue(issue: Issue): Observable<Issue> {
    const url = `${environment.baseUrl}${this.issuesUrl}`;
    return this.http.post<Issue>(url, issue, {
      headers: new HttpHeaders({ "Content-Type": "application/json" })
    });
  }

  updateIssue(issue: Issue): Observable<Issue> {
    const url = `${environment.baseUrl}${this.issuesUrl}`;
    return this.http.put<Issue>(url, issue, {
      headers: new HttpHeaders({ "Content-Type": "application/json" })
    });
  }

  getIssueById(id: number): Observable<Issue> {
    const url = `${environment.baseUrl}${this.issuesUrl}/${id}`;
    return this.http.get<Issue>(url);
  }

  getAssigneesByIssueId(id: number): Observable<User[]> {
    const url = `${environment.baseUrl}${this.issuesUrl}/${id}/assignees`;
    return this.http.get<User[]>(url);
  }

  insertAssignee(issueId: number, userId: number) {
    const url = `${environment.baseUrl}${
      this.issuesUrl
    }/${issueId}/assignees/${userId}`;
    return this.http.post<User[]>(url, {});
  }

  removeAssignee(issueId: number, userId: number) {
    const url = `${environment.baseUrl}${
      this.issuesUrl
    }/${issueId}/assignees/${userId}`;
    return this.http.delete<User[]>(url);
  }
}
