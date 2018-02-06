import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { MarkdownModule } from 'ngx-md';
import { of } from 'rxjs/observable/of';

import { SharedModule } from '../../shared/shared.module';
import { WikiPageSave } from '../shared/wiki-page-save.model';
import { WikiPage } from '../shared/wiki-page.model';
import { WikiPageService } from './../shared/wiki-page.service';
import { NewWikiComponent } from './new-wiki.component';

describe('NewWikiComponent', () => {
  let component: NewWikiComponent;
  let fixture: ComponentFixture<NewWikiComponent>;
  let wikiPageService: WikiPageService;
  let navigateSpy: jasmine.Spy;
  const repoId = 1;

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule,
          FormsModule,
          MarkdownModule
        ],
        providers: [WikiPageService],
        declarations: [NewWikiComponent]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(NewWikiComponent);
    component = fixture.componentInstance;
    wikiPageService = fixture.debugElement.injector.get(WikiPageService);
    navigateSpy = spyOn((<any>component).router, 'navigate');

    const wikiPage: WikiPage = {
      id: 1,
      name: 'name',
      content: 'content',
      repositoryId: repoId
    };

    spyOn(wikiPageService, 'saveWikiPage').and.returnValue(of(wikiPage));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able create new wiki page',
    async(() => {
      const wikiPage = new WikiPageSave('name', 'content', null);
      component.create(wikiPage);
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(wikiPageService.saveWikiPage).toHaveBeenCalled();
      });
    })
  );
});
