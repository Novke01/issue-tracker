import { TestBed } from '@angular/core/testing';
import { AuthGuardService } from './auth-guard.service';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('AuthGuardService', () => {

  let service: AuthGuardService;
  let authService: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]),
        HttpClientTestingModule
      ],
      providers: [
        AuthService
      ]
    });

    let router = TestBed.get(Router);

    authService = TestBed.get(AuthService);
    
    service = new AuthGuardService(authService, router);

    router.initialNavigation();

  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should block if user doesn\'t exist', () => {
    authService.user = null;
    let result = service.shouldPass('/');
    expect(result).toBeFalsy();
  });

  it('should pass if user doesn\'t exist', () => {
    authService.user = {
      id: 1,
      username: 'pera',
      firstName: 'Pera',
      lastName: 'Peric',
      email: 'pera@example.com',
      exp: 100000000
    };
    expect(service.shouldPass('/')).toBeTruthy();
  });

});
