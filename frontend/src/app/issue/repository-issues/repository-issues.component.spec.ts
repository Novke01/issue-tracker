import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryIssuesComponent } from './repository-issues.component';

describe('RepositoryIssuesComponent', () => {
  let component: RepositoryIssuesComponent;
  let fixture: ComponentFixture<RepositoryIssuesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RepositoryIssuesComponent ]
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
