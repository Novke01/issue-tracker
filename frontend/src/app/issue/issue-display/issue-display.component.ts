import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';

import { User } from '../../core/auth/user.model';
import { Issue } from '../shared/issue.model';
import { IssueService } from '../shared/issue.service';

@Component({
  selector: 'it-issue-display',
  templateUrl: './issue-display.component.html',
  styleUrls: ['./issue-display.component.css']
})
export class IssueDisplayComponent implements OnInit {
  issue: Issue = new Issue();
  form: FormGroup = new FormGroup({
    title: new FormControl(
      { value: 'title', disabled: true },
      Validators.required
    ),
    description: new FormControl({ value: 'description', disabled: true })
  });
  repositoryId: number;
  assignees: User[];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private issueService: IssueService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.repositoryId = +this.route.snapshot.paramMap.get('repoId');
    this.route.paramMap
      .switchMap((params: ParamMap) =>
        this.issueService.getIssueById(+params.get('issueId'))
      )
      .subscribe(issue => {
        this.issue = issue;
        this.issueService
          .getAssigneesByIssueId(this.issue.id)
          .subscribe(assignees => {
            this.assignees = assignees;
          });
      });
  }

  get title() {
    return this.form.get('title');
  }
  get description() {
    return this.form.get('description');
  }

  enableForm() {
    this.form.get('title').enable();
    this.form.get('description').enable();
  }

  disableForm() {
    this.form.get('title').disable();
    this.form.get('description').disable();
  }

  onUserAssigned(assignees) {
    const user = assignees[assignees.length - 1];
    this.issueService.insertAssignee(this.issue.id, user.id).subscribe(_ => {
      this.assignees = assignees;
    });
  }

  unassignUser(user) {
    this.issueService.removeAssignee(this.issue.id, user.id).subscribe(_ => {
      this.assignees = this.assignees.filter(function(a) {
        return a.id !== user.id;
      });
    });
  }

  onUpdateIssue() {
    if (this.form.valid) {
      this.issue.title = this.title.value;
      this.issue.description = this.description.value;
      this.issueService.updateIssue(this.issue).subscribe(
        issue => {
          console.log(issue);
          this.disableForm();
          this.snackBar.open('You have successfully updated an issue.', 'OK', {
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
