import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { AuthService } from '../../core/auth/auth.service';
import { SharedModule } from '../../shared/shared.module';
import { UserService } from '../../user/shared/user.service';
import { NewRepositoryComponent } from '../new-repository/new-repository.component';
import { RepositoryService } from '../shared/repository.service';
import { Repository } from './../shared/repository.model';
import { OwnedRepositoriesComponent } from './owned-repositories.component';

describe("OwnedRepositoriesComponent", () => {
  let component: OwnedRepositoriesComponent;
  let fixture: ComponentFixture<OwnedRepositoriesComponent>;
  let repositoryService: RepositoryService;
  let userService: UserService;
  let authService: AuthService;
  let repositories: Repository[];

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule,
          BrowserDynamicTestingModule
        ],
        providers: [
          RepositoryService,
          UserService,
          AuthService,
          { provide: NewRepositoryComponent, useValue: {} }
        ],
        declarations: [OwnedRepositoriesComponent, NewRepositoryComponent]
      })
        .overrideModule(BrowserDynamicTestingModule, {
          set: {
            entryComponents: [NewRepositoryComponent]
          }
        })
        .compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(OwnedRepositoriesComponent);
    component = fixture.componentInstance;
    let spy: jasmine.Spy;

    repositoryService = fixture.debugElement.injector.get(RepositoryService);
    userService = fixture.debugElement.injector.get(UserService);
    authService = fixture.debugElement.injector.get(AuthService);

    const repository = new Repository();
    repository.id = 1;
    repository.name = "repo1";
    repository.url = "https://github.com/user/repo1";
    repository.description = "description";
    repository.ownerId = 1;

    repositories = [repository];

    spyOn(repositoryService, "getOwnedRepositories").and.returnValue(
      of(repositories)
    );

    component.ngOnInit();

    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it(
    "should be able to get all owned repositories for that user",
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(repositoryService.getOwnedRepositories).toHaveBeenCalled();
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
