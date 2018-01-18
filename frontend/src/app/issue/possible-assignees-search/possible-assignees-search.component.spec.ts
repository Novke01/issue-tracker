import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PossibleAssigneesSearchComponent } from './possible-assignees-search.component';

describe('PossibleAssigneesSearchComponent', () => {
  let component: PossibleAssigneesSearchComponent;
  let fixture: ComponentFixture<PossibleAssigneesSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PossibleAssigneesSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PossibleAssigneesSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
