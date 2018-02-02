import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';

import { RepositoryService } from '../../repository/shared/repository.service';
import { SharedModule } from '../../shared/shared.module';
import { PossibleAssigneesSearchComponent } from './possible-assignees-search.component';

describe("PossibleAssigneesSearchComponent", () => {
  let component: PossibleAssigneesSearchComponent;
  let fixture: ComponentFixture<PossibleAssigneesSearchComponent>;

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
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
