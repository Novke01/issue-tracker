import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, NG_VALUE_ACCESSOR } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';

import { MilestoneService } from '../../milestone/shared/milestone.service';
import { RepositoryService } from '../../repository/shared/repository.service';
import { SharedModule } from '../../shared/shared.module';
import { IssueService } from '../shared/issue.service';
import { CreateIssueComponent } from './create-issue.component';

describe('CreateIssueComponent', () => {
  let component: CreateIssueComponent;
  let fixture: ComponentFixture<CreateIssueComponent>;

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
          MilestoneService,
          { provide: MatDialogRef, useValue: {} },
          { provide: MAT_DIALOG_DATA, useValue: [] },
          { provide: NG_VALUE_ACCESSOR}
        ],
        schemas: [CUSTOM_ELEMENTS_SCHEMA]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateIssueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
