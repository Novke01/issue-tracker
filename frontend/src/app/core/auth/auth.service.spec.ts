import { TestBed, inject } from "@angular/core/testing";
import { AuthService } from "./auth.service";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { LoginUser } from "./login-user.model";
import { HttpRequest } from "@angular/common/http";
import { AppConfig } from "../../app.config";
import { LoggedInUser } from "./logged-in-user.model";
import { JwtHelper } from "angular2-jwt";

describe('AuthService', () => {

  let service: AuthService;
  let httpMock: HttpTestingController;
  
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        AuthService
      ]
    });
    service = TestBed.get(AuthService);
    httpMock = TestBed.get(HttpTestingController);
    let jwtHelper = new JwtHelper();
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
      accessToken: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJ0ZXN0IiwiZmlyc3ROYW1lIjoidGVzdCIsImxhc3ROYW1lIjoidGVzdCIsImVtYWlsIjoidGVzdEBleGFtcGxlLmNvbSIsImV4cCI6NDY3MTI1NDg2MH0._yhko5xt2rFeObksul9pCDDL8CzJoD614ua9ThewNhA',
      refreshToken: 'lndKOGFToDIlBBNs01IyMgQpY3Gjkw'
    };

    service.login(loginData).subscribe(loggedInUser => {
      expect(localStorage.getItem('access_token')).toBeTruthy();
      expect(service.user).toBeTruthy();
    });

    httpMock.expectOne((req: HttpRequest<any>) => {
      const body: LoginUser = req.body;
      return req.url === AppConfig.baseUrl + 'api/auth/login' &&
             req.method === 'POST' &&
             req.headers.get('Content-Type') === 'application/json' &&
             body === loginData;
    }, 'POST to api/auth/login with user data in json format').flush(response, { status: 200, statusText: 'Ok'});

    httpMock.verify();

  });

  it('should be able to handle login rejection from server', () => {
    
    const loginData: LoginUser = {
      username: 'pera',
      password: 'testtestt'
    };

    service.login(loginData).subscribe(
      _ => {
        expect(localStorage.getItem('access_token')).toBeFalsy();
        expect(service.user).toBeFalsy();
      },
      err => {
          expect(err).toBeTruthy();
      });

    httpMock.expectOne((req: HttpRequest<any>) => {
      const body: LoginUser = req.body;
      return req.url === AppConfig.baseUrl + 'api/auth/login' &&
             req.method === 'POST' &&
             req.headers.get('Content-Type') === 'application/json' &&
             body === loginData;
    }, 'POST to api/auth/login with user data in json format').flush(null, { status: 400, statusText: 'BadRequest'});

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
    
    const refreshToken = 'lndKOGFToDIlBBNs01IyMgQpY3Gjkw';
    localStorage.setItem('refresh_token', refreshToken);

    const response: LoggedInUser = {
      accessToken: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJ0ZXN0IiwiZmlyc3ROYW1lIjoidGVzdCIsImxhc3ROYW1lIjoidGVzdCIsImVtYWlsIjoidGVzdEBleGFtcGxlLmNvbSIsImV4cCI6NDY3MTI1NDg2MH0._yhko5xt2rFeObksul9pCDDL8CzJoD614ua9ThewNhA',
      refreshToken: refreshToken
    };

    service.refreshToken().subscribe(_ => {
      expect(localStorage.getItem('access_token')).toBeTruthy();
      expect(service.user).toBeTruthy();
    });

    httpMock.expectOne((req: HttpRequest<any>) => {
      const token = req.body.token;
      return req.url === AppConfig.baseUrl + 'api/auth/refresh' &&
             req.method === 'POST' &&
             req.headers.get('Content-Type') === 'application/json' &&
             token === refreshToken;
    }, 'POST to api/auth/refresh with refresh token in json format').flush(response, { status: 200, statusText: 'Ok'});

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
    
    const refreshToken = 'lndKOGFToDIlBBNs01IyMgQpY3Gjkw';
    localStorage.setItem('refresh_token', refreshToken);

    service.refreshToken().subscribe(
      _ => {
        expect(localStorage.getItem('access_token')).toBeFalsy();
        expect(service.user).toBeFalsy();
      },
      err => expect(err).toBeTruthy()
    );

    httpMock.expectOne((req: HttpRequest<any>) => {
      const token = req.body.token;
      return req.url === AppConfig.baseUrl + 'api/auth/refresh' &&
             req.method === 'POST' &&
             req.headers.get('Content-Type') === 'application/json' &&
             token === refreshToken;
    }, 'POST to api/auth/refresh with refresh token in json format').flush(null, { status: 400, statusText: 'BadRequest'});

    httpMock.verify();

  });
  
});