import { HttpRequest } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { User } from '../../core/auth/user.model';
import { RegistrationUser } from './registration-user.model';
import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
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
      const body: RegistrationUser = req.body;
      return req.url === environment.baseUrl + 'api/user/registration' &&
             req.method === 'POST' &&
             req.headers.get('Content-Type') === 'application/json' &&
             body === dummyUser;
    }, 'POST to api/user/registration with user data in json format')
    .flush(responseUser, { status: 201, statusText: 'Created' });

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
      const body: RegistrationUser = req.body;
      return req.url === environment.baseUrl + 'api/user/registration' &&
             req.method === 'POST' &&
             req.headers.get('Content-Type') === 'application/json' &&
             body === dummyUser;
    }, 'POST to api/user/registration with user data in json format')
    .flush(null, { status: 400, statusText: 'BadRequest' });

    httpMock.verify();
  });

  it('should be able to get user data by id', () => {
    const id = 1;

    const responseUser: User = {
      id: id,
      firstName: 'Pera',
      lastName: 'Peric',
      username: 'pera',
      email: 'pera@example.com',
      exp: 0
    };

    service.getUserData(id).subscribe(
      result => expect(result).toBe(responseUser)
    );
    httpMock.expectOne((req: HttpRequest<any>) => {
      const body: RegistrationUser = req.body;
      return req.url === `${environment.baseUrl}api/user/${id}` &&
             req.method === 'GET';
    }, 'GET to api/user with user data in json format')
    .flush(responseUser, { status: 200, statusText: 'OK' });

    httpMock.verify();
  });

  it('should be able to handle rejection from server', () => {
    const id = 1;

    const responseUser: User = {
      id: id,
      firstName: 'Pera',
      lastName: 'Peric',
      username: 'pera',
      email: 'pera@example.com',
      exp: 0
    };

    service.getUserData(id).subscribe(
      result => expect(result).toBeFalsy(),
      err => expect(err).toBeTruthy()
    );

    httpMock.expectOne((req: HttpRequest<any>) => {
      const body: RegistrationUser = req.body;
      return req.url === `${environment.baseUrl}api/user/${id}` &&
             req.method === 'GET';
    }, 'GET to api/user with user data in json format')
    .flush(null, { status: 400, statusText: 'BadRequest' });

    httpMock.verify();
  });

});
