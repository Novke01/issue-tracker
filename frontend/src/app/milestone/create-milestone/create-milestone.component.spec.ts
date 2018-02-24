import { DatePipe } from '@angular/common';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { SharedModule } from '../../shared/shared.module';
import { Milestone } from '../shared/milestone.model';
import { MilestoneService } from '../shared/milestone.service';
import { CreateMilestoneComponent } from './create-milestone.component';

describe('CreateMilestoneComponent', () => {
  let component: CreateMilestoneComponent;
  let fixture: ComponentFixture<CreateMilestoneComponent>;
  let milestoneService: MilestoneService;
  let datepipe: DatePipe;

  const mockDatePipe = {
    transform(): void {}
  };

  const mockDialogRef = {
    close(): void {}
  };

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        declarations: [CreateMilestoneComponent],
        providers: [
          MilestoneService,
          { provide: MatDialogRef, useValue: mockDialogRef },
          { provide: MAT_DIALOG_DATA, useValue: [] },
          { provide: DatePipe, useValue: mockDatePipe }
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateMilestoneComponent);
    component = fixture.componentInstance;
    milestoneService = fixture.debugElement.injector.get(MilestoneService);
    datepipe = fixture.debugElement.injector.get(DatePipe);

    const milestone: Milestone = {
      id: 1,
      title: 'title',
      description: 'description',
      dueDate: '15/12/2018',
      repositoryId: 1
    };

    spyOn(milestoneService, 'createMilestone').and.returnValue(of(milestone));
    spyOn(datepipe, 'transform').and.returnValue(of('15/09/2018'));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able create new milestone',
    async(() => {
      component.title.setValue('title');
      component.description.setValue('description');
      component.dueDate.setValue(new Date());
      expect(component.form.valid).toBeTruthy();
      component.onCreateMilestone();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(milestoneService.createMilestone).toHaveBeenCalled();
      });
    })
  );
});
