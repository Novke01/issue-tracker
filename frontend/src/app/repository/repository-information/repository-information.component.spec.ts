import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { SharedModule } from '../../shared/shared.module';
import { RepositoryService } from '../shared/repository.service';
import { User } from './../../core/auth/user.model';
import { RepositoryInformationComponent } from './repository-information.component';

describe('RepositoryInformationComponent', () => {
  let component: RepositoryInformationComponent;
  let fixture: ComponentFixture<RepositoryInformationComponent>;
  let repositoryService: RepositoryService;
  let contributors: User[];
  let allContributors: User[];
  let owner: User;

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
        declarations: [RepositoryInformationComponent]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryInformationComponent);
    component = fixture.componentInstance;

    repositoryService = fixture.debugElement.injector.get(RepositoryService);

    const contributor = new User();
    contributor.id = 1;
    contributor.username = 'username';
    contributor.firstName = 'firstName';
    contributor.lastName = 'lastName';
    contributor.email = 'email@email.com';
    contributor.exp = 1111111;

    owner = new User();
    owner.id = 2;
    owner.username = 'username';
    owner.firstName = 'firstName';
    owner.lastName = 'lastName';
    owner.email = 'email@email.com';
    owner.exp = 1111111;

    contributors = [contributor];
    allContributors = [owner, contributor];

    spyOn(repositoryService, 'getContributorsByRepositoryId').and.returnValue(
      of(contributors)
    );
    spyOn(repositoryService, 'getOwnerByRepositoryId').and.returnValue(
      of(owner)
    );

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get all owned repositories for that user',
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(repositoryService.getOwnerByRepositoryId).toHaveBeenCalled();
        expect(
          repositoryService.getContributorsByRepositoryId
        ).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.contributors).toEqual(allContributors);
          expect(component.owner).toBe(owner);
        });
      });
    })
  );

  it(
    'should apply filter when value has been passed',
    async(() => {
      const name = 'Repository    with NAME ';
      component.applyFilter(name);
      expect(component.dataSource.filter).toBe(name.trim().toLowerCase());
    })
  );
});
