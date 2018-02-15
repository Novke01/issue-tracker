import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MatSnackBar } from '@angular/material';

import { PullRequestService } from '../shared/pull-request.service';
import { PullRequest } from './../shared/pull-request.model';

@Component({
  selector: 'it-create-pull-request',
  templateUrl: './create-pull-request.component.html',
  styleUrls: ['./create-pull-request.component.css']
})
export class CreatePullRequestComponent implements OnInit {
  form: FormGroup;
  control: FormControl = new FormControl();
  repositoryId: number;

  constructor(
    private formBuilder: FormBuilder,
    private pullRequestService: PullRequestService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<CreatePullRequestComponent>
  ) {}

  ngOnInit() {
    const fc = new FormControl();
    this.form = this.formBuilder.group({
      title: ['', Validators.required],
      url: ['', Validators.required]
    });
  }

  get title() {
    return this.form.get('title');
  }
  get url() {
    return this.form.get('url');
  }

  onCreatePullRequest() {
    if (this.form.valid) {
      const pullRequest = new PullRequest();
      pullRequest.repositoryId = this.repositoryId;
      pullRequest.title = this.title.value;
      pullRequest.url = this.url.value;

      this.pullRequestService.createPullRequest(pullRequest).subscribe(
        createdPullRequest => {
          this.dialogRef.close(createdPullRequest);
          this.snackBar.open(
            'You have successfully created an pull request.',
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
