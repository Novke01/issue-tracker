import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';

import { environment } from '../../../environments/environment';
import { Milestone } from './milestone.model';

@Injectable()
export class MilestoneService {
  private milestoneUrl = 'api/milestones';

  constructor(private http: HttpClient) {}

  getByRepositoryId(id: number): Observable<Milestone[]> {
    const url = `${environment.baseUrl}${this.milestoneUrl}/repository/${id}`;
    return this.http.get<Milestone[]>(url);
  }

  createMilestone(milestone: Milestone): Observable<Milestone> {
    const url = `${environment.baseUrl}${this.milestoneUrl}`;
    return this.http.post<Milestone>(url, milestone).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  remove(id: number): Observable<Milestone> {
    const url = `${environment.baseUrl}${this.milestoneUrl}/${id}`;
    return this.http.delete<Milestone>(url);
  }
}
