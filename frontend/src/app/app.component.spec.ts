import { TestBed, async, ComponentFixture } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { Component } from '@angular/core';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { Routes, Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'it-starter-page',
  template: '<div class="it-starter-page"></div>'
})
class StarterPageComponent { }

@Component({
  selector: 'it-home',
  template: '<div class="it-home"></div>'
})
class HomeComponent { }

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
        RouterTestingModule.withRoutes(testRoutes)
      ],
      declarations: [
        AppComponent,
        StarterPageComponent,
        HomeComponent
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
        const home = fixture.debugElement.query(By.css('.it-home'));
        expect(home).toBeTruthy();
      });
    });
  }));

});
