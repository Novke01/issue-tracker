import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryWikiComponent } from './repository-wiki.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '../../shared/shared.module';
import { FormsModule } from '@angular/forms';
import { MarkdownModule } from 'ngx-md';
import { WikiPageService } from '../shared/wiki-page.service';

describe('RepositoryWikiComponent', () => {
  let component: RepositoryWikiComponent;
  let fixture: ComponentFixture<RepositoryWikiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
        SharedModule,
        FormsModule,
        MarkdownModule
      ],
      providers : [ WikiPageService ],
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
