import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';

import { environment } from '../../../environments/environment';
import { Comment } from './comment.model';

@Injectable()
export class CommentService {
  private commentUrl = 'api/comments';

  constructor(private http: HttpClient) {}

  get(id: string): Observable<Comment> {
    const url = `${environment.baseUrl}${this.commentUrl}/${id}`;
    return this.http.get<Comment>(url);
  }

  getById(id: number, route: string): Observable<Comment[]> {
    const url = `${environment.baseUrl}${this.commentUrl}/${route}/${id}`;
    return this.http.get<Comment[]>(url);
  }

  createComment(comment: Comment): Observable<Comment> {
    const url = `${environment.baseUrl}${this.commentUrl}`;
    return this.http.post<Comment>(url, comment).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }
}
