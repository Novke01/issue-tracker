import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { SharedModule } from '../../shared/shared.module';
import { PullRequest } from '../shared/pull-request.model';
import { PullRequestService } from './../shared/pull-request.service';
import { RepositoryPullRequestsComponent } from './repository-pull-requests.component';

describe('RepositoryPullRequestsComponent', () => {
  let component: RepositoryPullRequestsComponent;
  let fixture: ComponentFixture<RepositoryPullRequestsComponent>;
  let pullRequestService: PullRequestService;
  const pullRequest: PullRequest = {
    id: 1,
    title: 'title',
    url: 'url',
    repositoryId: 1
  };
  const pullRequests = [pullRequest];

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        declarations: [RepositoryPullRequestsComponent],
        providers: [PullRequestService]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryPullRequestsComponent);
    component = fixture.componentInstance;
    pullRequestService = fixture.debugElement.injector.get(PullRequestService);

    spyOn(pullRequestService, 'getByRepositoryId').and.returnValue(
      of(pullRequests)
    );

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get all pull requests for that repository',
    async(() => {
      component.ngOnInit();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(pullRequestService.getByRepositoryId).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.pullRequests).toBe(pullRequests);
        });
      });
    })
  );
});
