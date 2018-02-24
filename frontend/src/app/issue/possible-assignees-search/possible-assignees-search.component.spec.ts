import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {
  async,
  ComponentFixture,
  TestBed,
  tick,
  fakeAsync
} from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { RepositoryService } from '../../repository/shared/repository.service';
import { SharedModule } from '../../shared/shared.module';
import { PossibleAssigneesSearchComponent } from './possible-assignees-search.component';
import { User } from '../../core/auth/user.model';

describe('PossibleAssigneesSearchComponent', () => {
  let component: PossibleAssigneesSearchComponent;
  let fixture: ComponentFixture<PossibleAssigneesSearchComponent>;
  let repositoryService: RepositoryService;
  let users: User[];

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          FormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        declarations: [PossibleAssigneesSearchComponent],
        providers: [RepositoryService],
        schemas: [CUSTOM_ELEMENTS_SCHEMA]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(PossibleAssigneesSearchComponent);
    component = fixture.componentInstance;

    repositoryService = fixture.debugElement.injector.get(RepositoryService);

    const user = new User();
    user.id = 1;
    user.username = 'username';
    user.firstName = 'firstName';
    user.lastName = 'lastName';
    user.email = 'email@email.com';
    user.exp = 1111111;

    users = [user];

    spyOn(repositoryService, 'searchOwnerAndContributors').and.returnValue(
      of(users)
    );

    component.ngOnInit();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be get owner and contributors of issue when search is called',
    fakeAsync(() => {
      component.searchBoxValue = 'search term';
      component.search();
      tick(400);
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(repositoryService.searchOwnerAndContributors).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          component.possibleAssignees$.subscribe(possibleAssignees =>
            expect(possibleAssignees).toEqual(users)
          );
        });
      });
    })
  );
});
