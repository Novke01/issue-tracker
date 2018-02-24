import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '../../shared/shared.module';
import { RepositoryLabelsComponent } from './repository-labels.component';
import { LabelService } from '../shared/label.service';
import { RepositoryService } from '../../repository/shared/repository.service';
import { Label } from '../shared/label.model';
import { of } from 'rxjs/observable/of';

describe('RepositoryLabelsComponent', () => {
  let component: RepositoryLabelsComponent;
  let fixture: ComponentFixture<RepositoryLabelsComponent>;
  let repositoryService: RepositoryService;
  let labelService: LabelService;
  let labels: Label[];

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
    repositoryService = fixture.debugElement.injector.get(RepositoryService);
    labelService = fixture.debugElement.injector.get(LabelService);

    const label = new Label();
    label.id = 1;
    label.repositoryId = 1;
    label.name = 'label1';
    label.color = '#ff0000';

    labels = [label];

    spyOn(repositoryService, 'getLabelsByRepositoryId').and.returnValue(
      of(labels)
    );
    spyOn(labelService, 'removeLabel').and.returnValue(of({}));

    component.repositoryId = 1;
    component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get all labels for that repository',
    async(() => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(repositoryService.getLabelsByRepositoryId).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.labels).toBe(labels);
        });
      });
    })
  );

  it(
    'should apply filter when value has been passed',
    async(() => {
      const name = 'Test name     WITH MULTIPLE SPACES   ';
      component.applyFilter(name);
      expect(component.dataSource.filter).toBe(name.trim().toLowerCase());
    })
  );

  it(
    'should be able to unassign user',
    async(() => {
      component.labels = labels;
      component.remove(labels[0].id);
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(labelService.removeLabel).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.labels).toEqual([]);
        });
      });
    })
  );
});
