import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { Repository } from './../repository/shared/repository.model';
import { RepositoryService } from './../repository/shared/repository.service';
import { SharedModule } from './../shared/shared.module';
import { RepositoryPageComponent } from './repository-page.component';

describe("RepositoryPageComponent", () => {
  let component: RepositoryPageComponent;
  let fixture: ComponentFixture<RepositoryPageComponent>;
  let repositoryService: RepositoryService;
  let repository: Repository;

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        declarations: [RepositoryPageComponent],
        providers: [RepositoryService],
        schemas: [CUSTOM_ELEMENTS_SCHEMA]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryPageComponent);
    component = fixture.componentInstance;
    repositoryService = fixture.debugElement.injector.get(RepositoryService);

    repository = {
      id: 1,
      name: "repo name",
      url: "repo url",
      description: "repo description",
      ownerId: 1
    };

    spyOn(repositoryService, "getRepositoryById").and.returnValue(
      of(repository)
    );

    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it(
    "should be able to get repository by id",
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(repositoryService.getRepositoryById).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.repository).toBe(repository);
        });
      });
    })
  );
});
