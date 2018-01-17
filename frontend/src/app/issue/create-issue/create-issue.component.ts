import { Component, OnInit } from '@angular/core';
import { FormGroup, AbstractControl, FormBuilder, Validators, FormControl } from '@angular/forms';
import { IssueService } from '../shared/issue.service';
import { MatSnackBar } from '@angular/material';
import { Issue } from '../shared/issue.model';
import { formDirectiveProvider } from '@angular/forms/src/directives/ng_form';

@Component({
  selector: 'it-create-issue',
  templateUrl: './create-issue.component.html',
  styleUrls: ['./create-issue.component.css']
})
export class CreateIssueComponent implements OnInit {

  createIssueForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private issueService: IssueService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    let fc = new FormControl();
    this.createIssueForm = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['']
    });
  }

  get title() { return this.createIssueForm.get('title'); }
  get description() { return this.createIssueForm.get('description'); }

  onCreateIssue() {
    if (this.createIssueForm.valid) {
      const issue = new Issue();
      issue.title = this.title.value;
      issue.description = this.description.value;
      issue.ownerId = -1;
      issue.assignees = [];

      console.log(issue);

      this.issueService.createIssue(issue).subscribe(
        issue => {
          console.log(issue)
          this.snackBar.open('You have successfully created an issue.', 'OK', {
            duration: 2000
          })
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
