import { Component, OnInit } from '@angular/core';
import { FormGroup, AbstractControl, FormBuilder, Validators, FormControl } from '@angular/forms';
import { IssueService } from '../shared/issue.service';
import { MatSnackBar, MatDialogRef } from '@angular/material';
import { Issue } from '../shared/issue.model';
import { formDirectiveProvider } from '@angular/forms/src/directives/ng_form';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../core/auth/user.model';

@Component({
  selector: 'it-create-issue',
  templateUrl: './create-issue.component.html',
  styleUrls: ['./create-issue.component.css']
})
export class CreateIssueComponent implements OnInit {

  form: FormGroup;
  control: FormControl = new FormControl();
  repositoryId: number;
  assignees: User[] = [];


  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private issueService: IssueService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<CreateIssueComponent>,
  ) {}

  ngOnInit() {
    let fc = new FormControl();
    this.form = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['']
    });
  }

  get title() { return this.form.get('title'); }
  get description() { return this.form.get('description'); }

  onUserAssigned(assignees){
    this.assignees= assignees;
  }

  unassignUser(user){
    this.assignees = this.assignees.filter(function( a ) {
      return a.id !== user.id;
    });
  }

  onCreateIssue() {
    if (this.form.valid) {
      var issue = new Issue();
      issue.repositoryId = this.repositoryId;
      issue.title = this.title.value;
      issue.description = this.description.value;
      issue.ownerId = -1;
      issue.assignees = this.assignees.map(a => a.id);

      this.issueService.createIssue(issue).subscribe(
        issue => {
          console.log(issue)
          this.dialogRef.close(issue);
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
