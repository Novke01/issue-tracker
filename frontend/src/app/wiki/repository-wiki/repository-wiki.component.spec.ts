import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { MarkdownModule } from 'ngx-md';
import { of } from 'rxjs/observable/of';

import { SharedModule } from '../../shared/shared.module';
import { WikiPage } from '../shared/wiki-page.model';
import { WikiPageService } from '../shared/wiki-page.service';
import { RepositoryWikiComponent } from './repository-wiki.component';

describe("RepositoryWikiComponent", () => {
  let component: RepositoryWikiComponent;
  let fixture: ComponentFixture<RepositoryWikiComponent>;
  let wikiPageService: WikiPageService;
  const wikiPage: WikiPage = {
    id: 1,
    name: "wiki page name",
    content: "wiki page content",
    repositoryId: 1
  };
  const wikiPages = [wikiPage];

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
        declarations: [RepositoryWikiComponent]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryWikiComponent);
    component = fixture.componentInstance;
    wikiPageService = fixture.debugElement.injector.get(WikiPageService);

    spyOn(wikiPageService, "getWikiPageByRepositoryId").and.returnValue(
      of(wikiPages)
    );

    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it(
    "should be able to get all contributed repositories for that user",
    async(() => {
      component.ngOnInit();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(wikiPageService.getWikiPageByRepositoryId).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.wikiPages).toBe(wikiPages);
        });
      });
    })
  );
});
