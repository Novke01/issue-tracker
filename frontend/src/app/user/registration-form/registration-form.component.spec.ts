import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationFormComponent } from './registration-form.component';
import { SharedModule } from '../../shared/shared.module';
import { UserService } from '../shared/user.service';
import { of } from 'rxjs/observable/of';
import { User } from '../../core/auth/user.model';
import { HttpClientModule } from '@angular/common/http';

describe('RegistrationFormComponent', () => {

  let component: RegistrationFormComponent;
  let fixture: ComponentFixture<RegistrationFormComponent>;
  let userService: UserService;
  let spy: jasmine.Spy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        SharedModule,
        HttpClientModule
      ],
      declarations: [ 
        RegistrationFormComponent 
      ],
      providers: [
        UserService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationFormComponent);
    component = fixture.componentInstance;
    
    userService = fixture.debugElement.injector.get(UserService);

    let user: User = {
      id: 1,
      username: 'pera',
      firstName: 'Pera',
      lastName: 'Peric',
      email: 'pera@example.com',
      exp: 100000000
    };

    spy = spyOn(userService, 'register').and.returnValue(of(user));

    component.ngOnInit();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('username is not valid if it is empty', () => {
    component.username.setValue('');
    expect(component.username.invalid).toBeTruthy();
  });

  it('username is valid if something is entered', () => {
    component.username.setValue('pera');
    expect(component.username.valid).toBeTruthy();
  });

  it('firstName is not valid if it is empty', () => {
    component.firstName.setValue('');
    expect(component.firstName.invalid).toBeTruthy();
  });

  it('firstName is valid if something is entered', () => {
    component.firstName.setValue('Pera');
    expect(component.firstName.valid).toBeTruthy();
  });

  it('lastName is not valid if it is empty', () => {
    component.lastName.setValue('');
    expect(component.lastName.invalid).toBeTruthy();
  });

  it('lastName is valid if something is entered', () => {
    component.lastName.setValue('Peric');
    expect(component.lastName.valid).toBeTruthy();
  });

  it('email is not valid if it doesn\'t have standard email format', () => {
    component.email.setValue('aaa');
    expect(component.email.invalid).toBeTruthy();
  });

  it('email is valid if it has standard email format', () => {
    component.email.setValue('pera@example.com');
    expect(component.email.valid).toBeTruthy();
  });

  it('password is not valid if it is less than 8 characters long', () => {
    component.password.setValue('test');
    expect(component.password.invalid).toBeTruthy();
  });

  it('password is valid if it is at least 8 characters long', () => {
    component.password.setValue('testtest');
    expect(component.password.valid).toBeTruthy();
  });

  it('form is invalid if password and confirmedPassword aren\'t equal', () => {
    component.username.setValue('pera');
    component.firstName.setValue('Pera');
    component.lastName.setValue('Peric');
    component.email.setValue('pera@example.com');
    component.password.setValue('testtest');
    component.confirmedPassword.setValue('testtess');
    expect(component.signUpForm.invalid).toBeTruthy();
  });

  it('form is valid if it has all field valid and password and confirmedPassword are equal', () => {
    component.username.setValue('pera');
    component.firstName.setValue('Pera');
    component.lastName.setValue('Peric');
    component.email.setValue('pera@example.com');
    component.password.setValue('testtest');
    component.confirmedPassword.setValue('testtest');
    expect(component.signUpForm.valid).toBeTruthy();
  });

  it('should be able to send registration request if form is valid', async(() => {
    component.username.setValue('pera');
    component.firstName.setValue('Pera');
    component.lastName.setValue('Peric');
    component.email.setValue('pera@example.com');
    component.password.setValue('testtest');
    component.confirmedPassword.setValue('testtest');
    expect(component.signUpForm.valid).toBeTruthy();
    component.onRegister();
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(userService.register).toHaveBeenCalled();
    });
  }));

  it('should not be able to send registration request if form is invalid', async(() => {
    component.username.setValue('pera');
    component.firstName.setValue('Pera');
    component.lastName.setValue('Peric');
    component.email.setValue('peraexample.com');
    component.password.setValue('testtest');
    component.confirmedPassword.setValue('testtest');
    expect(component.signUpForm.invalid).toBeTruthy();
    component.onRegister();
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(userService.register).toHaveBeenCalledTimes(0);
    });
  }));

});
