import { SharedModule } from './../shared/shared.module';
import { RepositoryService } from './../repository/shared/repository.service';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryPageComponent } from './repository-page.component';
import { RouterTestingModule } from '@angular/router/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('RepositoryPageComponent', () => {
  let component: RepositoryPageComponent;
  let fixture: ComponentFixture<RepositoryPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
        SharedModule
      ],
      declarations: [ RepositoryPageComponent ],
      providers: [ RepositoryService ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
