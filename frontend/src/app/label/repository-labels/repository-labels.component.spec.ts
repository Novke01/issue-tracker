import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '../../shared/shared.module';
import { RepositoryLabelsComponent } from './repository-labels.component';
import { LabelService } from '../shared/label.service';
import { RepositoryService } from '../../repository/shared/repository.service';

describe('RepositoryLabelsComponent', () => {
  let component: RepositoryLabelsComponent;
  let fixture: ComponentFixture<RepositoryLabelsComponent>;

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        providers: [LabelService, RepositoryService],
        declarations: [RepositoryLabelsComponent]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryLabelsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
