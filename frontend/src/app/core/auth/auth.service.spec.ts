import { HttpRequest } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { Routes } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';

import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';
import { LoggedInUser } from './logged-in-user.model';
import { LoginUser } from './login-user.model';

@Component({
  selector: 'it-home-page',
  template: '<div class="it-home-page"></div>'
})
export class HomePageComponent {}

@Component({
  selector: 'it-starter-page',
  template: '<div class="it-starter-page"></div>'
})
export class StarterPageComponent {}

const appRoutes: Routes = [
  { path: 'login', component: StarterPageComponent },
  { path: '', component: HomePageComponent }
];

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const accessToken =
    'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJ0ZXN0I' +
    'iwiZmlyc3ROYW1lIjoidGVzdCIsImxhc3ROYW1lIjoidGVzdCIsImVtYWlsIjoidGVzdEB' +
    'leGFtcGxlLmNvbSIsImV4cCI6NDY3MTI1NDg2MH0._yhko5xt2rFeObksul9pCDDL8CzJoD614ua9ThewNhA';
  const refreshToken = 'lndKOGFToDIlBBNs01IyMgQpY3Gjkw';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(appRoutes),
        HttpClientTestingModule
      ],
      declarations: [StarterPageComponent, HomePageComponent],
      providers: [AuthService]
    });
    service = TestBed.get(AuthService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be able to login with valid data', () => {
    const loginData: LoginUser = {
      username: 'test',
      password: 'testtest'
    };

    const response: LoggedInUser = {
      accessToken: accessToken,
      refreshToken: refreshToken
    };

    service.login(loginData).subscribe(() => {
      expect(localStorage.getItem('access_token')).toBeTruthy();
      expect(service.user).toBeTruthy();
    });

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: LoginUser = req.body;
        return (
          req.url === environment.baseUrl + 'api/auth/login' &&
          req.method === 'POST' &&
          req.headers.get('Content-Type') === 'application/json' &&
          body === loginData
        );
      }, 'POST to api/auth/login with user data in json format')
      .flush(response, { status: 200, statusText: 'OK' });

    httpMock.verify();
  });

  it('should be able to handle login rejection from server', () => {
    const loginData: LoginUser = {
      username: 'pera',
      password: 'testtestt'
    };

    service.login(loginData).subscribe(
      () => {
        expect(localStorage.getItem('access_token')).toBeFalsy();
        expect(service.user).toBeFalsy();
      },
      err => {
        expect(err).toBeTruthy();
      }
    );

    httpMock.expectOne((req: HttpRequest<any>) => {
      const body: LoginUser = req.body;
      return (
        req.url === environment.baseUrl + 'api/auth/login' &&
        req.method === 'POST' &&
        req.headers.get('Content-Type') === 'application/json' &&
        body === loginData
      );
    }, 'POST to api/auth/login with user data in json format')
    .flush(null, { status: 400, statusText: 'BadRequest' });

    httpMock.verify();
  });

  it('should be able to refresh token with valid refreshToken', () => {
    service.user = {
      id: 1,
      username: 'pera',
      firstName: 'Pera',
      lastName: 'Peric',
      email: 'pera@example.com',
      exp: 10000000000000
    };

    localStorage.setItem('refresh_token', refreshToken);

    const response: LoggedInUser = {
      accessToken: accessToken,
      refreshToken: refreshToken
    };

    service.refreshToken().subscribe(() => {
      expect(localStorage.getItem('access_token')).toBeTruthy();
      expect(service.user).toBeTruthy();
    });

    httpMock.expectOne((req: HttpRequest<any>) => {
      const token = req.body.token;
      return (
        req.url === environment.baseUrl + 'api/auth/refresh' &&
        req.method === 'POST' &&
        req.headers.get('Content-Type') === 'application/json' &&
        token === refreshToken
      );
    }, 'POST to api/auth/refresh with refresh token in json format')
    .flush(response, { status: 200, statusText: 'OK' });

    httpMock.verify();
  });

  it('should be able to handle token refresh rejection from server', () => {
    service.user = {
      id: 1,
      username: 'pera',
      firstName: 'Pera',
      lastName: 'Peric',
      email: 'pera@example.com',
      exp: 10000000000000
    };

    localStorage.setItem('refresh_token', refreshToken);

    service.refreshToken().subscribe(
      () => {
        expect(localStorage.getItem('access_token')).toBeFalsy();
        expect(service.user).toBeFalsy();
      },
      err => expect(err).toBeTruthy()
    );

    httpMock.expectOne((req: HttpRequest<any>) => {
      const token = req.body.token;
      return (
        req.url === environment.baseUrl + 'api/auth/refresh' &&
        req.method === 'POST' &&
        req.headers.get('Content-Type') === 'application/json' &&
        token === refreshToken
      );
    }, 'POST to api/auth/refresh with refresh token in json format')
    .flush(null, { status: 400, statusText: 'BadRequest' });

    httpMock.verify();
  });

  it('should be able to logout user', () => {
    localStorage.setItem('access_token', accessToken);
    localStorage.setItem('refresh_token', refreshToken);
    service.user = {
      id: 1,
      username: 'pera',
      firstName: 'Pera',
      lastName: 'Peric',
      email: 'pera@example.com',
      exp: 10000000000000
    };
    service.logout().subscribe(() => {
      expect(localStorage.getItem('access_token')).toBeFalsy();
      expect(localStorage.getItem('refresh_token')).toBeFalsy();
      expect(service.user).toBeFalsy();
    });
  });
});
