import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';

import { SharedModule } from '../../shared/shared.module';
import { IssueService } from '../shared/issue.service';
import { CreateIssueComponent } from './create-issue.component';
import { RepositoryService } from '../../repository/shared/repository.service';
import { UserService } from '../../user/shared/user.service';
import { AuthService } from '../../core/auth/auth.service';
import { User } from '../../core/auth/user.model';
import { Issue } from '../shared/issue.model';
import { Label } from '../../label/shared/label.model';
import { of } from 'rxjs/observable/of';

describe('CreateIssueComponent', () => {
  let component: CreateIssueComponent;
  let fixture: ComponentFixture<CreateIssueComponent>;
  let userService: UserService;
  let authService: AuthService;
  let repositoryService: RepositoryService;
  let issueService: IssueService;
  let labels: Label[];
  let assignees: User[];
  const issue = new Issue();

  issue.id = 1;
  issue.repositoryId = 1;
  issue.title = 'issue1';
  issue.description = 'description';
  issue.ownerId = 1;
  issue.labels = [1, 2];
  issue.assignees = [1, 2];
  issue.status = 'OPENED';

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
          SharedModule
        ],
        declarations: [CreateIssueComponent],
        providers: [
          IssueService,
          RepositoryService,
          UserService,
          AuthService,
          { provide: MatDialogRef, useValue: mockDialogRef },
          { provide: MAT_DIALOG_DATA, useValue: [] }
        ],
        schemas: [CUSTOM_ELEMENTS_SCHEMA]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateIssueComponent);
    component = fixture.componentInstance;

    userService = fixture.debugElement.injector.get(UserService);
    authService = fixture.debugElement.injector.get(AuthService);
    repositoryService = fixture.debugElement.injector.get(RepositoryService);
    issueService = fixture.debugElement.injector.get(IssueService);

    const user = new User();
    user.id = 1;
    user.username = 'username';
    user.firstName = 'firstName';
    user.lastName = 'lastName';
    user.email = 'email@email.com';
    user.exp = 1111111;

    const label1 = new Label();
    label1.id = 1;
    label1.repositoryId = 1;
    label1.name = 'Bug';
    label1.color = '#FF0000';

    const label2 = new Label();
    label2.id = 2;
    label2.repositoryId = 1;
    label2.name = 'Feature';
    label2.color = '#FF0080';

    labels = [label1, label2];
    assignees = [user];
    authService.user = user;

    spyOn(repositoryService, 'getLabelsByRepositoryId').and.returnValue(
      of(labels)
    );
    spyOn(issueService, 'createIssue').and.returnValue(of(issue));

    component.ngOnInit();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get repository labels',
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(repositoryService.getLabelsByRepositoryId).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.repositoryLabels).toEqual(labels);
        });
      });
    })
  );

  it(
    'should be able to unassign user',
    async(() => {
      component.assignees = assignees;
      component.unassignUser(assignees[0]);
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(component.assignees).toEqual([]);
      });
    })
  );

  it(
    'should be able to create issue when data is valid',
    async(() => {
      component.repositoryId = 1;
      component.title.setValue('new issue');
      component.description.setValue('new description');
      component.labels.setValue(labels);
      component.assignees = assignees;
      expect(component.form.valid).toBeTruthy();
      component.onCreateIssue();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(issueService.createIssue).toHaveBeenCalled();
      });
    })
  );

  it(
    'should not be able to create issue when data is invalid',
    async(() => {
      expect(component.form.invalid).toBeTruthy();
      component.onCreateIssue();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(issueService.createIssue).toHaveBeenCalledTimes(0);
      });
    })
  );
});
