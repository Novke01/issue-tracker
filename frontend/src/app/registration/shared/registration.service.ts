import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';
import 'rxjs/add/observable/throw';

import { AppConfig } from '../../app.config';
import { RegistrationUser } from './registration-user.model';
import { RegisteredUser } from './registered-user.model';



const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class RegistrationService {

  private registrationUrl = 'api/registration';

  constructor(private http: HttpClient) { }

  register(user: RegistrationUser): Observable<RegisteredUser> {
    const url = AppConfig.baseUrl + this.registrationUrl;
    return this.http.post<RegisteredUser>(url, user, httpOptions).pipe(
      catchError(err => Observable.throw(new Error(err.error)))
    );
  }

}
