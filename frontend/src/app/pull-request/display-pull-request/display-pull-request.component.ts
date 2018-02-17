import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { ActivatedRoute, ParamMap } from '@angular/router';

import { PullRequest } from './../shared/pull-request.model';
import { PullRequestService } from './../shared/pull-request.service';

@Component({
  selector: 'it-display-pull-request',
  templateUrl: './display-pull-request.component.html',
  styleUrls: ['./display-pull-request.component.css']
})
export class DisplayPullRequestComponent implements OnInit {
  displayedColumns = ['title', 'url'];
  dataSource: MatTableDataSource<PullRequest>;
  pullRequest: PullRequest;

  constructor(
    private route: ActivatedRoute,
    private pullRequestService: PullRequestService
  ) {}

  ngOnInit() {
    this.route.paramMap
      .switchMap((params: ParamMap) =>
        this.pullRequestService.get(params.get('pullRequestId'))
      )
      .subscribe(result => {
        this.pullRequest = result;
        this.dataSource = new MatTableDataSource([this.pullRequest]);
      });
  }
}
