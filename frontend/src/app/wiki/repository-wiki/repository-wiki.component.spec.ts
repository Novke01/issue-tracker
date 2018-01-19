import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryWikiComponent } from './repository-wiki.component';

describe('RepositoryWikiComponent', () => {
  let component: RepositoryWikiComponent;
  let fixture: ComponentFixture<RepositoryWikiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RepositoryWikiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryWikiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
