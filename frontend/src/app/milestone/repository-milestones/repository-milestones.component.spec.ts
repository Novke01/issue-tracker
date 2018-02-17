import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { SharedModule } from '../../shared/shared.module';
import { Milestone } from '../shared/milestone.model';
import { MilestoneService } from './../shared/milestone.service';
import { RepositoryMilestonesComponent } from './repository-milestones.component';

describe('RepositoryMilestonesComponent', () => {
  let component: RepositoryMilestonesComponent;
  let fixture: ComponentFixture<RepositoryMilestonesComponent>;
  let milestoneService: MilestoneService;
  const milestone: Milestone = {
    id: 1,
    title: 'title',
    description: 'description',
    dueDate: '15/12/2018',
    repositoryId: 1
  };
  const milestones = [milestone];

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        declarations: [RepositoryMilestonesComponent],
        providers: [MilestoneService]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryMilestonesComponent);
    component = fixture.componentInstance;
    milestoneService = fixture.debugElement.injector.get(MilestoneService);

    spyOn(milestoneService, 'getByRepositoryId').and.returnValue(
      of(milestones)
    );

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get all milestones for that repository',
    async(() => {
      component.ngOnInit();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(milestoneService.getByRepositoryId).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.milestones).toBe(milestones);
        });
      });
    })
  );
});
