import { Location } from '@angular/common';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Router, Routes } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';

import { AppComponent } from './app.component';
import { AuthService } from './core/auth/auth.service';
import { SharedModule } from './shared/shared.module';

@Component({
  selector: 'it-starter-page',
  template: '<div class="it-starter-page"></div>'
})
class StarterPageComponent {}

@Component({
  selector: 'it-home-page',
  template: '<div class="it-home-page"></div>'
})
class HomeComponent {}

const testRoutes: Routes = [
  { path: 'login', component: StarterPageComponent },
  { path: '', component: HomeComponent }
];

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;
  let router: Router;
  let location: Location;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(testRoutes),
        SharedModule,
        HttpClientTestingModule
      ],
      declarations: [
        AppComponent,
        StarterPageComponent,
        HomeComponent
      ],
      providers: [
        AuthService
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    router = TestBed.get(Router);
    location = TestBed.get(Location);

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.debugElement.componentInstance;

    router.initialNavigation();
  });

  it('should create the app', async(() => {
    expect(component).toBeTruthy();
  }));

  it('should have starter page component on /login path', async(() => {
    router.navigate(['/login']).then(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(location.path()).toBe('/login');
        const sp = fixture.debugElement.query(By.css('.it-starter-page'));
        expect(sp).toBeTruthy();
      });
    });
  }));

  it('should have home page component on / path', async(() => {
    router.navigate(['/']).then(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(location.path()).toBe('/');
        const home = fixture.debugElement.query(By.css('.it-home-page'));
        expect(home).toBeTruthy();
      });
    });
  }));

});
