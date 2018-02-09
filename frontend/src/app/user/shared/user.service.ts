import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';

import { environment } from '../../../environments/environment';
import { User } from '../../core/auth/user.model';
import { RegistrationUser } from './registration-user.model';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class UserService {

  private userUrl = 'api/user';
  private registrationUrl = 'api/user/registration';

  constructor(private http: HttpClient) { }

  register(user: RegistrationUser): Observable<User> {
    const url = `${environment.baseUrl}${this.registrationUrl}`;
    return this.http.post<User>(url, user, httpOptions).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  getAll(): Observable<User[]> {
    const url = `${environment.baseUrl}${this.userUrl}`;
    return this.http.get<User[]>(url, httpOptions).pipe(
      catchError(err => {
        return Observable.throw(new Error(err.error));
      })
    );
  }

  getHome() {
    const url = `${environment.baseUrl}api/user/hello`;
    return this.http.get(url);
  }

  getUserData(id: Number): Observable<User> {
    const url = `${environment.baseUrl}${this.userUrl}/${id}`;
    return this.http.get<User>(url);
  }

}
