import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpRequest } from '@angular/common/http';

import { UserService } from './user.service';
import { RegistrationUser } from './registration-user.model';
import { HttpClientModule } from '@angular/common/http';
import { AppConfig } from '../../app.config';
import { User } from '../../core/auth/user.model';

describe('UserService', () => {

  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        UserService
      ]
    });
    service = TestBed.get(UserService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be able to register new user', () => {
    const dummyUser: RegistrationUser = {
      firstName: 'Pera',
      lastName: 'Peric',
      username: 'pera',
      password: 'testtest',
      email: 'pera@example.com'
    };

    service.register(dummyUser).subscribe();
    httpMock.expectOne((req: HttpRequest<any>) => {
      const body: RegistrationUser = req.body
      return req.url === AppConfig.baseUrl + 'api/user/registration' &&
             req.method === 'POST' &&
             req.headers.get('Content-Type') === 'application/json' &&
             body === dummyUser;
    }, 'POST to api/user/registration with user data in json format').flush({
      id: 1,
      firstName: dummyUser.firstName,
      lastName: dummyUser.lastName,
      username: dummyUser.username,
      password: dummyUser.password,
      email: dummyUser.email
    });

    httpMock.verify();

  });

  it('should be able to register new user', () => {
    const dummyUser: RegistrationUser = {
      firstName: 'Pera',
      lastName: 'Peric',
      username: 'pera',
      password: 'testtest',
      email: 'pera@example.com'
    };

    const responseUser: User = {
      id: 1,
      firstName: dummyUser.firstName,
      lastName: dummyUser.lastName,
      username: dummyUser.username,
      email: dummyUser.email,
      exp: 0
    };

    service.register(dummyUser).subscribe(user => {
      expect(user === responseUser).toBeTruthy();
    });
    httpMock.expectOne((req: HttpRequest<any>) => {
      const body: RegistrationUser = req.body
      return req.url === AppConfig.baseUrl + 'api/user/registration' &&
             req.method === 'POST' &&
             req.headers.get('Content-Type') === 'application/json' &&
             body === dummyUser;
    }, 'POST to api/user/registration with user data in json format').flush(responseUser, { status: 201, statusText: 'Created' });

    httpMock.verify();

  });

  it('should be able to handle rejection from server', () => {
    const dummyUser: RegistrationUser = {
      firstName: 'Pera',
      lastName: 'Peric',
      username: 'pera',
      password: 'testtest',
      email: 'pera@example.com'
    };

    const responseUser: User = {
      id: 1,
      firstName: dummyUser.firstName,
      lastName: dummyUser.lastName,
      username: dummyUser.username,
      email: dummyUser.email,
      exp: 0
    };

    service.register(dummyUser).subscribe(
      user => {
        console.log(user);
        expect(user).toBeFalsy();
      },
      err => {
        expect(err).toBeTruthy();
      }
    );
    httpMock.expectOne((req: HttpRequest<any>) => {
      const body: RegistrationUser = req.body
      return req.url === AppConfig.baseUrl + 'api/user/registration' &&
             req.method === 'POST' &&
             req.headers.get('Content-Type') === 'application/json' &&
             body === dummyUser;
    }, 'POST to api/user/registration with user data in json format').flush(null, { status: 400, statusText: 'BadRequest' });

    httpMock.verify();

  });

});
