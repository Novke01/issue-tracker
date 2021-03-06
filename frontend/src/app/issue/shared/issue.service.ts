import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../environments/environment';
import { User } from '../../core/auth/user.model';
import { Label } from '../../label/shared/label.model';
import { Issue } from './issue.model';

@Injectable()
export class IssueService {
  private issuesUrl = 'api/issues';

  constructor(private http: HttpClient) {}

  createIssue(issue: Issue): Observable<Issue> {
    const url = `${environment.baseUrl}${this.issuesUrl}`;
    return this.http.post<Issue>(url, issue, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }

  updateIssue(issue: Issue): Observable<Issue> {
    const url = `${environment.baseUrl}${this.issuesUrl}`;
    return this.http.put<Issue>(url, issue, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
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

  getLabelsByIssueId(id: number): Observable<Label[]> {
    const url = `${environment.baseUrl}${this.issuesUrl}/${id}/labels`;
    return this.http.get<Label[]>(url);
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

  insertLabel(issueId: number, labelid: number) {
    const url = `${environment.baseUrl}${
      this.issuesUrl
    }/${issueId}/labels/${labelid}`;
    return this.http.post<Label>(url, {});
  }

  removeLabel(issueId: number, labelId: number) {
    const url = `${environment.baseUrl}${
      this.issuesUrl
    }/${issueId}/labels/${labelId}`;
    return this.http.delete<Label>(url);
  }

  remove(id: number): Observable<Issue> {
    const url = `${environment.baseUrl}${this.issuesUrl}/${id}`;
    return this.http.delete<Issue>(url);
  }
}
