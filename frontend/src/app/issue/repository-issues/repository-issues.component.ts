import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';

import { Milestone } from '../../milestone/shared/milestone.model';
import { RepositoryService } from '../../repository/shared/repository.service';
import { CreateIssueComponent } from '../create-issue/create-issue.component';
import { Issue } from '../shared/issue.model';
import { IssueService } from '../shared/issue.service';
import { MilestoneService } from './../../milestone/shared/milestone.service';

@Component({
  selector: 'it-repository-issues',
  templateUrl: './repository-issues.component.html',
  styleUrls: ['./repository-issues.component.css']
})
export class RepositoryIssuesComponent implements OnInit {
  displayedColumns = [
    'title',
    'description',
    'status',
    'milestoneTitle',
    'actions'
  ];
  issues: Issue[];
  dataSource: MatTableDataSource<Issue>;
  repositoryId: number;
  milestones: Map<number, Milestone> = new Map<number, Milestone>();

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private repositoryService: RepositoryService,
    private milestoneService: MilestoneService,
    private issueService: IssueService,
    private dialog: MatDialog,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.repositoryId = +this.route.snapshot.paramMap.get('id');

    this.milestoneService
      .getByRepositoryId(this.repositoryId)
      .subscribe(milestones => {
        for (const milestone of milestones) {
          this.milestones[milestone.id] = milestone;
        }
        this.repositoryService
          .getIssuesByRepositoryId(this.repositoryId)
          .subscribe(issues => {
            for (const issue of issues) {
              if (this.milestones[issue.milestoneId] !== undefined) {
                issue.milestoneTitle = this.milestones[issue.milestoneId].title;
              }
            }
            this.issues = issues;
            this.dataSource = new MatTableDataSource(this.issues);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          });
      });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  openCreateIssueDialog() {
    const dialogRef = this.dialog.open(CreateIssueComponent, {
      hasBackdrop: false,
      width: '500px'
    });

    dialogRef.componentInstance.repositoryId = this.repositoryId;

    dialogRef.afterClosed().subscribe(newIssue => {
      if (newIssue !== '') {
        if (typeof newIssue.milestoneId !== 'undefined') {
          this.milestones = new Map<number, Milestone>();
          this.milestoneService
            .getByRepositoryId(this.repositoryId)
            .subscribe(milestones => {
              for (const milestone of milestones) {
                this.milestones[milestone.id] = milestone;
              }
              newIssue.milestoneTitle = this.milestones[
                newIssue.milestoneId
              ].title;
            });
        }
        this.issues.push(newIssue);
        this.dataSource = new MatTableDataSource(this.issues);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        while (this.dataSource.paginator.hasNextPage()) {
          this.dataSource.paginator.nextPage();
        }
      }
    });
  }

  remove(id: number) {
    this.issueService.remove(id).subscribe(() => {
      this.issues = this.issues.filter(function(l) {
        return l.id !== id;
      });
      this.dataSource = new MatTableDataSource(this.issues);
    });
  }
}
