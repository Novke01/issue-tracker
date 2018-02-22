import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute} from '@angular/router';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs/observable/of';

import { ProfileComponent } from './profile.component';
import { SharedModule } from '../../shared/shared.module';
import { UserService } from '../shared/user.service';
import { User } from '../../core/auth/user.model';

@Component({
  selector: 'it-contributed-repositories',
  template: '<div class="it-contributed-repositories"></div>'
})
export class ContributedRepositoriesComponent { }

@Component({
  selector: 'it-owned-repositories',
  template: '<div class="it-owned-repositories"></div>'
})
export class OwnedRepositoriesComponent { }

describe('ProfileComponent', () => {

  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let spy: jasmine.Spy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        SharedModule,
        HttpClientTestingModule
      ],
      providers: [
        UserService,
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({
              id: 1
            })
          }
        }
      ],
      declarations: [
        ProfileComponent,
        ContributedRepositoriesComponent,
        OwnedRepositoriesComponent
      ]
    }).compileComponents();

  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;

    const userService = fixture.debugElement.injector.get(UserService);
    const user: User = {
      id: 1,
      username: 'pera',
      firstName: 'Pera',
      lastName: 'Peric',
      email: 'pera@example.com',
      exp: 100000000
    };

    spy = spyOn(userService, 'getUserData').and.returnValue(of(user));

    fixture.detectChanges();

  });

  it('should create', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then( () => {
      expect(component).toBeTruthy();
      expect(component.user).toBeTruthy();
      const contributedRepos = fixture.debugElement.query(By.css('.it-contributed-repositories'));
      expect(contributedRepos).toBeTruthy();
      const ownedRepos = fixture.debugElement.query(By.css('.it-owned-repositories'));
      expect(ownedRepos).toBeTruthy();
    });
  }));

});
