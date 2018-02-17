import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';

import { RepositoryService } from '../../repository/shared/repository.service';
import { SharedModule } from '../../shared/shared.module';
import { MilestoneService } from './../../milestone/shared/milestone.service';
import { RepositoryIssuesComponent } from './repository-issues.component';
import { Issue } from '../shared/issue.model';
import { of } from 'rxjs/observable/of';
import { Milestone } from '../../milestone/shared/milestone.model';

describe('RepositoryIssuesComponent', () => {
  let component: RepositoryIssuesComponent;
  let fixture: ComponentFixture<RepositoryIssuesComponent>;
  let repositoryService: RepositoryService;
  let milestoneService: MilestoneService;
  let issues: Issue[];
  let milestones: Milestone[];

  const milestone = new Milestone();
  milestone.id = 1;
  milestone.repositoryId = 1;
  milestone.title = 'milestone1';
  milestone.description = 'milestone desc';
  milestone.dueDate = '';

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        providers: [RepositoryService, MilestoneService],
        declarations: [RepositoryIssuesComponent],
        schemas: [CUSTOM_ELEMENTS_SCHEMA]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryIssuesComponent);
    component = fixture.componentInstance;
    repositoryService = fixture.debugElement.injector.get(RepositoryService);
    milestoneService = fixture.debugElement.injector.get(MilestoneService);

    const issue = new Issue();
    issue.id = 1;
    issue.repositoryId = 1;
    issue.title = 'issue1';
    issue.description = 'description';
    issue.ownerId = 1;
    issue.labels = [1, 2];
    issue.assignees = [1, 2];
    issue.milestoneId = 1;
    issue.milestoneTitle = 'milestone1';
    issue.created = 1518810162338;
    issue.status = 'OPENED';

    issues = [issue];
    milestones = [milestone];

    spyOn(repositoryService, 'getIssuesByRepositoryId').and.returnValue(
      of(issues)
    );
    spyOn(milestoneService, 'getByRepositoryId').and.returnValue(
      of(milestones)
    );

    component.repositoryId = 1;
    component.ngOnInit();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get all issues and milestones for that repository',
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(milestoneService.getByRepositoryId).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.milestones[milestone.id]).toBe(milestones[0]);
          expect(repositoryService.getIssuesByRepositoryId).toHaveBeenCalled();
          fixture.detectChanges();
          fixture.whenStable().then(() => {
            expect(component.issues).toBe(issues);
          });
        });
      });
    })
  );

  it(
    'should apply filter when value has been passed',
    async(() => {
      const title = 'Test title     WITH MULTIPLE SPACES   ';
      component.applyFilter(title);
      expect(component.dataSource.filter).toBe(title.trim().toLowerCase());
    })
  );
});
