import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryInformationComponent } from './repository-information.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '../../shared/shared.module';
import { RepositoryService } from '../shared/repository.service';

describe('RepositoryInformationComponent', () => {
  let component: RepositoryInformationComponent;
  let fixture: ComponentFixture<RepositoryInformationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
        SharedModule
      ],
      providers: [
        RepositoryService
      ],
      declarations: [ RepositoryInformationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
