import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryIssuesComponent } from './repository-issues.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '../../shared/shared.module';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RepositoryService } from '../../repository/shared/repository.service';

describe('RepositoryIssuesComponent', () => {
  let component: RepositoryIssuesComponent;
  let fixture: ComponentFixture<RepositoryIssuesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
        SharedModule
      ],
      providers: [
        RepositoryService
      ],
      declarations: [ RepositoryIssuesComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
