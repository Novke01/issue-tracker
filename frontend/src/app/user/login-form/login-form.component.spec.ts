import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { AuthService } from '../../core/auth/auth.service';
import { LoggedInUser } from '../../core/auth/logged-in-user.model';
import { SharedModule } from '../../shared/shared.module';
import { UserService } from '../shared/user.service';
import { LoginFormComponent } from './login-form.component';

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;
  let spy: jasmine.Spy;
  let authService: AuthService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
        SharedModule
      ],
      declarations: [LoginFormComponent],
      providers: [AuthService, UserService]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;

    authService = fixture.debugElement.injector.get(AuthService);
    const loggedInUser: LoggedInUser = {
      accessToken: '',
      refreshToken: ''
    };
    spy = spyOn(authService, 'login').and.returnValue(of(loggedInUser));

    component.ngOnInit();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form should be valid if password is 8 or more characters long and username has been entered', () => {
    component.username.setValue('test');
    component.password.setValue('testtest');
    const form = component.signInForm;
    expect(form.valid).toBeTruthy();
  });

  it('username is not valid if it is empty', () => {
    component.username.setValue('');
    expect(component.username.invalid).toBeTruthy();
  });

  it('username is valid if something is entered', () => {
    component.username.setValue('test');
    expect(component.username.valid).toBeTruthy();
  });

  it('password is not valid if it is empty', () => {
    component.password.setValue('');
    expect(component.password.invalid).toBeTruthy();
  });

  it('password is not valid if it contains less than 8 characters', () => {
    component.password.setValue('test');
    expect(component.password.invalid).toBeTruthy();
  });

  it('username is valid if it contains at least 8 characters', () => {
    component.password.setValue('testtest');
    expect(component.password.valid).toBeTruthy();
  });

  it(
    'should be able to send login request if form is valid',
    async(() => {
      component.username.setValue('test');
      component.password.setValue('testtest');
      expect(component.signInForm.valid).toBeTruthy();
      component.onLogin();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(authService.login).toHaveBeenCalled();
      });
    })
  );

  it(
    'should not be able to send login request if form is invalid',
    async(() => {
      component.username.setValue('test');
      component.password.setValue('testtes');
      expect(component.signInForm.invalid).toBeTruthy();
      component.onLogin();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(authService.login).toHaveBeenCalledTimes(0);
      });
    })
  );
});
