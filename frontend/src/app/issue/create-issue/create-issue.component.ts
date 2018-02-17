import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import { MatDialogRef, MatSnackBar } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';

import { User } from '../../core/auth/user.model';
import { Issue } from '../shared/issue.model';
import { IssueService } from '../shared/issue.service';
import { RepositoryService } from '../../repository/shared/repository.service';
import { Label } from '../../label/shared/label.model';
import { AuthService } from '../../core/auth/auth.service';

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
  repositoryLabels: Label[] = [];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private issueService: IssueService,
    private repositoryService: RepositoryService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<CreateIssueComponent>
  ) {}

  ngOnInit() {
    this.repositoryService
      .getLabelsByRepositoryId(this.repositoryId)
      .subscribe(result => {
        this.repositoryLabels = result;
      });
    const fc = new FormControl();
    this.form = this.formBuilder.group({
      title: ['', Validators.required],
      description: [''],
      labels: ['']
    });
  }

  get title() {
    return this.form.get('title');
  }
  get description() {
    return this.form.get('description');
  }
  get labels() {
    return this.form.get('labels');
  }

  onUserAssigned(assignees) {
    this.assignees = assignees;
  }

  unassignUser(user) {
    this.assignees = this.assignees.filter(function(a) {
      return a.id !== user.id;
    });
  }

  onCreateIssue() {
    if (this.form.valid) {
      const issue = new Issue();
      issue.repositoryId = this.repositoryId;
      issue.title = this.title.value;
      issue.description = this.description.value;
      issue.ownerId = this.authService.user.id;
      issue.assignees = this.assignees.map(a => a.id);
      issue.labels = [];
      if (this.labels.value) {
        issue.labels = this.labels.value.map(l => l.id);
      }

      this.issueService.createIssue(issue).subscribe(
        createdIssue => {
          console.log(createdIssue);
          this.dialogRef.close(createdIssue);
          this.snackBar.open('You have successfully created an issue.', 'OK', {
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
