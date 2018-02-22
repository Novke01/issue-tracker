import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MatSnackBar } from '@angular/material';

import { Milestone } from '../shared/milestone.model';
import { MilestoneService } from '../shared/milestone.service';

@Component({
  selector: 'it-create-milestone',
  templateUrl: './create-milestone.component.html',
  styleUrls: ['./create-milestone.component.css']
})
export class CreateMilestoneComponent implements OnInit {
  form: FormGroup;
  control: FormControl = new FormControl();
  repositoryId: number;

  constructor(
    private formBuilder: FormBuilder,
    private milestoneService: MilestoneService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<CreateMilestoneComponent>,
    private datepipe: DatePipe
  ) {}

  ngOnInit() {

    this.form = this.formBuilder.group({
      title: ['', Validators.required],
      description: [''],
      dueDate: ['', Validators.required]
    });
  }

  get title() {
    return this.form.get('title');
  }
  get description() {
    return this.form.get('description');
  }
  get dueDate() {
    return this.form.get('dueDate');
  }

  onCreateMilestone() {
    if (this.form.valid) {
      const milestone = new Milestone();
      milestone.repositoryId = this.repositoryId;
      milestone.title = this.title.value;
      milestone.description = this.description.value;
      milestone.dueDate = this.datepipe.transform(
        this.dueDate.value,
        'dd/MM/yyyy'
      );

      this.milestoneService.createMilestone(milestone).subscribe(
        createdMilestone => {
          this.dialogRef.close(createdMilestone);
          this.snackBar.open(
            'You have successfully created an milestone.',
            'OK',
            {
              duration: 2000
            }
          );
        },
        err => {
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    }
  }
}
