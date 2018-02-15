import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';

import { PullRequest } from '../shared/pull-request.model';
import { CreatePullRequestComponent } from './../create-pull-request/create-pull-request.component';
import { PullRequestService } from './../shared/pull-request.service';

@Component({
  selector: 'it-repository-pull-requests',
  templateUrl: './repository-pull-requests.component.html',
  styleUrls: ['./repository-pull-requests.component.css']
})
export class RepositoryPullRequestsComponent implements OnInit {
  repositoryId: number;
  pullRequests: PullRequest[] = [];
  displayedColumns = ['title', 'url'];
  dataSource: MatTableDataSource<PullRequest>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private pullRequestService: PullRequestService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.repositoryId = +this.route.snapshot.paramMap.get('id');
    this.pullRequestService
      .getByRepositoryId(this.repositoryId)
      .subscribe(result => {
        this.pullRequests = result;
        this.dataSource = new MatTableDataSource(this.pullRequests);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  openCreatePullRequestDialog() {
    const dialogRef = this.dialog.open(CreatePullRequestComponent, {
      hasBackdrop: false
    });

    dialogRef.componentInstance.repositoryId = this.repositoryId;

    dialogRef.afterClosed().subscribe(newPullRequest => {
      if (newPullRequest !== '') {
        this.pullRequests.push(newPullRequest);
        this.dataSource = new MatTableDataSource(this.pullRequests);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        while (this.dataSource.paginator.hasNextPage()) {
          this.dataSource.paginator.nextPage();
        }
      }
    });
  }
}
