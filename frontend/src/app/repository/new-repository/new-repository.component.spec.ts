import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { User } from '../../core/auth/user.model';
import { SharedModule } from '../../shared/shared.module';
import { Repository } from '../shared/repository.model';
import { RepositoryService } from '../shared/repository.service';
import { AuthService } from './../../core/auth/auth.service';
import { UserService } from './../../user/shared/user.service';
import { NewRepositoryComponent } from './new-repository.component';

describe('NewRepositoryComponent', () => {
  let component: NewRepositoryComponent;
  let fixture: ComponentFixture<NewRepositoryComponent>;
  let userService: UserService;
  let authService: AuthService;
  let repositoryService: RepositoryService;
  let users: User[];
  const repository = new Repository();

  repository.id = 1;
  repository.name = 'repo1';
  repository.url = 'https://github.com/user/repo1';
  repository.description = 'description';
  repository.ownerId = 1;

  const mockDialogRef = {
    close(): void {}
  };

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule,
          MatDialogModule
        ],
        providers: [
          RepositoryService,
          UserService,
          AuthService,
          { provide: MatDialogRef, useValue: mockDialogRef },
          { provide: MAT_DIALOG_DATA, useValue: [] }
        ],
        declarations: [NewRepositoryComponent]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(NewRepositoryComponent);
    component = fixture.componentInstance;

    userService = fixture.debugElement.injector.get(UserService);
    authService = fixture.debugElement.injector.get(AuthService);
    repositoryService = fixture.debugElement.injector.get(RepositoryService);

    const user = new User();
    user.id = 1;
    user.username = 'username';
    user.firstName = 'firstName';
    user.lastName = 'lastName';
    user.email = 'email@email.com';
    user.exp = 1111111;

    const otherUser = new User();
    user.id = 2;
    user.username = 'usernameOther';
    user.firstName = 'firstNameOther';
    user.lastName = 'lastNameOther';
    user.email = 'emailOther@email.com';
    user.exp = 1111111;

    users = [otherUser];
    authService.user = user;

    spyOn(userService, 'getAll').and.returnValue(of(users));
    spyOn(repositoryService, 'saveRepository').and.returnValue(of(repository));

    component.ngOnInit();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get all users',
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(userService.getAll).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.users).toEqual(users);
        });
      });
    })
  );

  it(
    'should be able to save repository when data is valid',
    async(() => {
      component.name.setValue('new repository');
      component.description.setValue('new description');
      component.url.setValue('new url');
      component.control.setValue(users);
      expect(component.form.valid).toBeTruthy();
      component.submit();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(repositoryService.saveRepository).toHaveBeenCalled();
      });
    })
  );

  it(
    'should not be able to save repository when data is invalid',
    async(() => {
      expect(component.form.invalid).toBeTruthy();
      component.submit();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(repositoryService.saveRepository).toHaveBeenCalledTimes(0);
      });
    })
  );
});
