import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewWikiComponent } from './new-wiki.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '../../shared/shared.module';
import { FormsModule } from '@angular/forms';
import { MarkdownModule } from 'ngx-md';
import { WikiPageService } from '../shared/wiki-page.service';

describe('NewWikiComponent', () => {
  let component: NewWikiComponent;
  let fixture: ComponentFixture<NewWikiComponent>;

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
      declarations: [ NewWikiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewWikiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
