import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';

import { RepositoryService } from '../../repository/shared/repository.service';
import { SharedModule } from '../../shared/shared.module';
import { IssueService } from '../shared/issue.service';
import { IssueDisplayComponent } from './issue-display.component';
import { Issue } from '../shared/issue.model';
import { of } from 'rxjs/observable/of';
import { User } from '../../core/auth/user.model';
import { Label } from '../../label/shared/label.model';

describe('IssueDisplayComponent', () => {
  let component: IssueDisplayComponent;
  let fixture: ComponentFixture<IssueDisplayComponent>;
  let issueService: IssueService;
  let repositoryService: RepositoryService;
  let issue: Issue;
  let assignees: User[];
  let labels: Label[];

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        declarations: [IssueDisplayComponent],
        providers: [IssueService, RepositoryService],
        schemas: [CUSTOM_ELEMENTS_SCHEMA]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(IssueDisplayComponent);
    component = fixture.componentInstance;
    issueService = fixture.debugElement.injector.get(IssueService);
    repositoryService = fixture.debugElement.injector.get(RepositoryService);

    issue = {
      id: 1,
      repositoryId: 1,
      title: 'issue1',
      description: 'description',
      ownerId: 1,
      labels: [1, 2],
      assignees: [1, 2],
      milestoneId: 1,
      milestoneTitle: 'milestone1',
      created: 1518810162338,
      status: 'OPENED'
    };

    const user = new User();
    user.id = 1;
    user.username = 'username';
    user.firstName = 'firstName';
    user.lastName = 'lastName';
    user.email = 'email@email.com';
    user.exp = 1111111;

    const label = new Label();
    label.id = 1;
    label.repositoryId = 1;
    label.name = 'Bug';
    label.color = '#FF0000';

    labels = [label];
    assignees = [user];

    spyOn(issueService, 'getIssueById').and.returnValue(of(issue));
    spyOn(issueService, 'getLabelsByIssueId').and.returnValue(of(labels));
    spyOn(issueService, 'getAssigneesByIssueId').and.returnValue(of(assignees));
    spyOn(repositoryService, 'getLabelsByRepositoryId').and.returnValue(
      of(labels)
    );
    spyOn(issueService, 'removeAssignee').and.returnValue(of({}));
    spyOn(issueService, 'updateIssue').and.returnValue(of(issue));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get issue',
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(issueService.getIssueById).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.issue).toEqual(issue);
        });
      });
    })
  );

  it(
    'should be able to get issue assignees',
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(issueService.getAssigneesByIssueId).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.assignees).toEqual(assignees);
        });
      });
    })
  );

  it(
    'should be able to get issue labels',
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(issueService.getLabelsByIssueId).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.issueLabels).toEqual(labels);
        });
      });
    })
  );

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
        expect(issueService.removeAssignee).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.assignees).toEqual([]);
        });
      });
    })
  );

  it(
    'should be able to enable disabled form',
    async(() => {
      component.enableForm();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(component.form.enabled).toBeTruthy();
      });
    })
  );

  it(
    'should be able to disable enabled form',
    async(() => {
      component.enableForm();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        component.disableForm();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.form.disabled).toBeTruthy();
        });
      });
    })
  );

  it(
    'should be able to update issue when data is valid',
    async(() => {
      component.enableForm();
      fixture.detectChanges();
      component.repositoryId = 1;
      component.title.setValue('updated issue');
      component.description.setValue('updated description');
      expect(component.form.valid).toBeTruthy();
      component.onUpdateIssue();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(issueService.updateIssue).toHaveBeenCalled();
        expect(component.form.disabled).toBeTruthy();
      });
    })
  );

  it(
    'should not be able to update issue when data is invalid',
    async(() => {
      component.enableForm();
      fixture.detectChanges();
      component.repositoryId = 1;
      component.title.setValue('');
      expect(component.form.invalid).toBeTruthy();
      component.onUpdateIssue();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(issueService.updateIssue).toHaveBeenCalledTimes(0);
      });
    })
  );
});
