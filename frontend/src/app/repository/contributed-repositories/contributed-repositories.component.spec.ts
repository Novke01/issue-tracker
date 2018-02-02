import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { SharedModule } from '../../shared/shared.module';
import { Repository } from './../shared/repository.model';
import { RepositoryService } from './../shared/repository.service';
import { ContributedRepositoriesComponent } from './contributed-repositories.component';

describe("ContributedRepositoriesComponent", () => {
  let component: ContributedRepositoriesComponent;
  let fixture: ComponentFixture<ContributedRepositoriesComponent>;
  let repositoryService: RepositoryService;
  let repositories: Repository[];

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        providers: [RepositoryService],
        declarations: [ContributedRepositoriesComponent]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(ContributedRepositoriesComponent);
    component = fixture.componentInstance;
    let spy: jasmine.Spy;

    repositoryService = fixture.debugElement.injector.get(RepositoryService);
    const repository = new Repository();
    repository.id = 1;
    repository.name = "repo1";
    repository.url = "https://github.com/user/repo1";
    repository.description = "description";
    repository.ownerId = 1;

    repositories = [repository];

    spy = spyOn(
      repositoryService,
      "getContributedRepositories"
    ).and.returnValue(of(repositories));

    component.ngOnInit();

    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it(
    "should be able to get all contributed repositories for that user",
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(repositoryService.getContributedRepositories).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.repositories).toBe(repositories);
        });
      });
    })
  );

  it(
    "should apply filter when value has been passed",
    async(() => {
      const name = "First Name     Last Name   ";
      component.applyFilter(name);
      expect(component.dataSource.filter).toBe(name.trim().toLowerCase());
    })
  );
});
