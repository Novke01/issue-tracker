import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import { MatDialogRef, MatSnackBar } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';

import { Label } from '../shared/label.model';
import { LabelService } from '../shared/label.service';

@Component({
  selector: 'it-create-label',
  templateUrl: './create-label.component.html',
  styleUrls: ['./create-label.component.css']
})
export class CreateLabelComponent implements OnInit {
  form: FormGroup;
  control: FormControl = new FormControl();
  repositoryId: number;
  selectedColor: String;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private labelService: LabelService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<CreateLabelComponent>
  ) {}

  ngOnInit() {
    const fc = new FormControl();
    this.form = this.formBuilder.group({
      name: ['', Validators.required]
    });
  }

  get name() {
    return this.form.get('name');
  }
  get color() {
    return this.form.get('color');
  }

  onCreateLabel() {
    if (this.form.valid && this.selectedColor !== undefined) {
      const label = new Label();
      label.name = this.name.value;
      label.color = this.selectedColor;
      label.repositoryId = this.repositoryId;

      this.labelService.createLabel(label).subscribe(
        newLabel => {
          console.log(newLabel);
          this.dialogRef.close(newLabel);
          this.snackBar.open('You have successfully created a label.', 'OK', {
            duration: 2000
          });
        },
        err => {
          console.log(err);
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    }
  }
}
