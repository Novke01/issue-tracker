import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserService } from '../user/shared/user.service';
import { HomePageComponent } from './home-page.component';

@Component({
  selector: 'it-contributed-repositories',
  template: '<div class="it-contributed-repositories"></div>'
})
export class ContributedRepositoriesComponent {}

@Component({
  selector: 'it-owned-repositories',
  template: '<div class="it-owned-repositories"></div>'
})
export class OwnerRepositoriesComponent {}

describe('HomeComponent', () => {

  let component: HomePageComponent;
  let fixture: ComponentFixture<HomePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [
        HomePageComponent,
        OwnerRepositoriesComponent,
        ContributedRepositoriesComponent
      ],
      providers: [UserService]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
