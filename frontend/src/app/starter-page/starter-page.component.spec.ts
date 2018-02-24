import { Component } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { SharedModule } from '../shared/shared.module';
import { StarterPageComponent } from './starter-page.component';

@Component({
  selector: 'it-registration-form',
  template: '<div class="it-registration-form"></div>'
})
export class RegistrationFormComponent {}

@Component({
  selector: 'it-login-form',
  template: '<div class="it-login-form"></div>'
})
export class LoginFormComponent {}

describe('StarterPageComponent', () => {
  let component: StarterPageComponent;
  let fixture: ComponentFixture<StarterPageComponent>;

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [SharedModule],
        declarations: [
          StarterPageComponent,
          RegistrationFormComponent,
          LoginFormComponent
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(StarterPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have registration form', () => {
    const rf = fixture.debugElement.query(By.css('it-registration-form'));
    expect(rf).toBeTruthy();
  });

  it(
    'should have login form',
    async(() => {
      const tab = fixture.debugElement.query(By.css('mat-tab-group'));
      tab.componentInstance.selectedIndex = 1;
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        const lf = fixture.debugElement.query(By.css('div.it-login-form'));
        expect(lf).toBeTruthy();
      });
    })
  );
});
