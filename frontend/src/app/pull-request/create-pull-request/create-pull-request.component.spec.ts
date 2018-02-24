import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { SharedModule } from '../../shared/shared.module';
import { PullRequest } from './../shared/pull-request.model';
import { PullRequestService } from './../shared/pull-request.service';
import { CreatePullRequestComponent } from './create-pull-request.component';

describe('CreatePullRequestComponent', () => {
  let component: CreatePullRequestComponent;
  let fixture: ComponentFixture<CreatePullRequestComponent>;
  let pullRequestService: PullRequestService;

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
        declarations: [CreatePullRequestComponent],
        providers: [
          PullRequestService,
          { provide: MatDialogRef, useValue: mockDialogRef },
          { provide: MAT_DIALOG_DATA, useValue: [] }
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(CreatePullRequestComponent);
    component = fixture.componentInstance;
    pullRequestService = fixture.debugElement.injector.get(PullRequestService);

    const pullRequest: PullRequest = {
      id: 1,
      title: 'title',
      url: 'url',
      repositoryId: 1
    };

    spyOn(pullRequestService, 'createPullRequest').and.returnValue(
      of(pullRequest)
    );
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able create new pull request',
    async(() => {
      component.title.setValue('title');
      component.url.setValue('url');
      expect(component.form.valid).toBeTruthy();
      component.onCreatePullRequest();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(pullRequestService.createPullRequest).toHaveBeenCalled();
      });
    })
  );
});
