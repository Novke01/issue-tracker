import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { SharedModule } from '../../shared/shared.module';
import { PullRequestService } from '../shared/pull-request.service';
import { PullRequest } from './../shared/pull-request.model';
import { DisplayPullRequestComponent } from './display-pull-request.component';

describe('DisplayPullRequestComponent', () => {
  let component: DisplayPullRequestComponent;
  let fixture: ComponentFixture<DisplayPullRequestComponent>;
  let pullRequestService: PullRequestService;
  const pullRequest = new PullRequest();

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        providers: [PullRequestService],
        declarations: [DisplayPullRequestComponent],
        schemas: [CUSTOM_ELEMENTS_SCHEMA]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayPullRequestComponent);
    component = fixture.componentInstance;

    pullRequestService = fixture.debugElement.injector.get(PullRequestService);

    pullRequest.id = 1;
    pullRequest.title = 'title';
    pullRequest.url = 'url';
    pullRequest.repositoryId = 1;

    spyOn(pullRequestService, 'get').and.returnValue(of(pullRequest));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get pull request information',
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(pullRequestService.get).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.pullRequest).toBe(pullRequest);
        });
      });
    })
  );
});
