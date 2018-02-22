import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import * as _ from 'underscore';

import { User } from '../../core/auth/user.model';
import { Label } from '../../label/shared/label.model';
import { RepositoryService } from '../../repository/shared/repository.service';
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
  labelsForm: FormGroup = new FormGroup({
    labels: new FormControl({ value: 'labels' })
  });
  repositoryId: number;
  issueId: number;
  assignees: User[];
  issueLabels: Label[];
  repositoryLabels: Label[];
  selectedLabelsIds: Array<number>;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private issueService: IssueService,
    private repositoryService: RepositoryService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.repositoryId = +this.route.snapshot.paramMap.get('repoId');
    this.issueId = +this.route.snapshot.paramMap.get('issueId');
    this.issueService.getIssueById(this.issueId).subscribe(issue => {
      this.issue = issue;
    });
    this.issueService
      .getAssigneesByIssueId(this.issueId)
      .subscribe(assignees => {
        this.assignees = assignees;
      });
    this.issueService.getLabelsByIssueId(this.issueId).subscribe(labels => {
      this.issueLabels = labels;
      this.selectedLabelsIds = this.issueLabels.map(function(l) {
        return l.id;
      });
    });
    this.repositoryService
      .getLabelsByRepositoryId(this.repositoryId)
      .subscribe(result => {
        this.repositoryLabels = result;
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

  addOrRemoveLabel() {
    const addedLabels = _.difference(
      this.selectedLabelsIds,
      this.issueLabels.map(function(l) {
        return l.id;
      })
    );
    const removedLabels = _.difference(
      this.issueLabels.map(function(l) {
        return l.id;
      }),
      this.selectedLabelsIds
    );
    let labelId;
    if ((labelId = addedLabels.pop())) {
      this.issueService.insertLabel(this.issue.id, labelId).subscribe(a => {
        const l = this.repositoryLabels.filter(function(obj) {
          return obj.id === labelId;
        })[0];
        this.issueLabels.push(l);
      });
    } else if ((labelId = removedLabels.pop())) {
      this.issueService.removeLabel(this.issue.id, labelId).subscribe(a => {
        this.issueLabels = this.issueLabels.filter(function(l) {
          return l.id !== labelId;
        });
      });
    }
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

  closeIssue() {
    if (this.issue.status === 'OPENED') {
      this.issue.status = 'CLOSED';
      this.issueService.updateIssue(this.issue).subscribe(
        issue => {
          this.snackBar.open('You have successfully closed an issue.', 'OK', {
            duration: 2000
          });
        },
        err => {
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    }
  }

  reopenIssue() {
    if (this.issue.status === 'CLOSED') {
      this.issue.status = 'OPENED';
      this.issueService.updateIssue(this.issue).subscribe(
        issue => {
          this.snackBar.open('You have successfully reopened an issue.', 'OK', {
            duration: 2000
          });
        },
        err => {
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    }
  }

  onUpdateIssue() {
    if (this.form.valid) {
      this.issue.title = this.title.value;
      this.issue.description = this.description.value;
      this.issueService.updateIssue(this.issue).subscribe(
        issue => {
          this.disableForm();
          this.snackBar.open('You have successfully updated an issue.', 'OK', {
            duration: 2000
          });
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
