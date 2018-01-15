import { TestBed } from '@angular/core/testing';
import { AuthGuardService } from './auth-guard.service';
import { AuthService } from './auth.service';
import { Router, Routes  } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component } from '@angular/core';

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

describe('AuthGuardService', () => {

  let service: AuthGuardService;
  let authService: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(appRoutes),
        HttpClientTestingModule
      ],
      declarations: [
        HomePageComponent,
        StarterPageComponent
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
