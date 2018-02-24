import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '../../shared/shared.module';
import { CreateLabelComponent } from './create-label.component';
import { LabelService } from '../shared/label.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Label } from '../shared/label.model';
import { of } from 'rxjs/observable/of';

describe('CreateLabelComponent', () => {
  let component: CreateLabelComponent;
  let fixture: ComponentFixture<CreateLabelComponent>;

  let labelService: LabelService;
  const label = new Label();

  label.id = 1;
  label.repositoryId = 1;
  label.name = 'label1';
  label.color = '#ff0000';

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
        providers: [
          LabelService,
          { provide: MatDialogRef, useValue: mockDialogRef },
          { provide: MAT_DIALOG_DATA, useValue: [] }
        ],
        declarations: [CreateLabelComponent]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateLabelComponent);
    component = fixture.componentInstance;

    labelService = fixture.debugElement.injector.get(LabelService);

    spyOn(labelService, 'createLabel').and.returnValue(of(label));
    component.repositoryId = 1;
    component.ngOnInit();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to create label when data is valid',
    async(() => {
      component.repositoryId = 1;
      component.name.setValue('new label');
      component.selectedColor = '#ff0000';
      expect(component.form.valid).toBeTruthy();
      component.onCreateLabel();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(labelService.createLabel).toHaveBeenCalled();
      });
    })
  );

  it(
    'should not be able to create label when data is invalid',
    async(() => {
      expect(component.form.invalid).toBeTruthy();
      component.onCreateLabel();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(labelService.createLabel).toHaveBeenCalledTimes(0);
      });
    })
  );
});
