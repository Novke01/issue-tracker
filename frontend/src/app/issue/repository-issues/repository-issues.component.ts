import { SharedModule } from '../../shared/shared.module';
import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { UserService } from '../../user/shared/user.service';

import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Issue } from '../shared/issue.model';
import { RepositoryService } from '../../repository/shared/repository.service';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { CreateIssueComponent } from '../create-issue/create-issue.component';

@Component({
  selector: 'it-repository-issues',
  templateUrl: './repository-issues.component.html',
  styleUrls: ['./repository-issues.component.css']
})
export class RepositoryIssuesComponent implements OnInit {

  displayedColumns = ['title', 'description', 'status'];
  issues: Issue[];
  dataSource: MatTableDataSource<Issue>;
  newIssue: Issue = new Issue();
  repositoryId: number;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private repositoryService: RepositoryService, private dialog: MatDialog, private route: ActivatedRoute) {}

  ngOnInit() {
    this.repositoryId = +this.route.snapshot.paramMap.get('id');
    
    this.repositoryService.getIssuesByRepositoryId(this.repositoryId).subscribe(result => {
      this.issues = result;
      this.dataSource = new MatTableDataSource(this.issues);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  openCreateIssueDialog() {
    var dialogRef = this.dialog.open(CreateIssueComponent, {
      hasBackdrop: false,
      data: { newIssue: this.newIssue}
    });

    dialogRef.componentInstance.repositoryId = this.repositoryId;

    dialogRef.afterClosed().subscribe(issue => {
      if(issue != ""){
        this.newIssue = issue;
        this.issues.push(this.newIssue);
        this.newIssue = new Issue();
        this.dataSource = new MatTableDataSource(this.issues);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        while (this.dataSource.paginator.hasNextPage()) {
          this.dataSource.paginator.nextPage();
        }
      }
    });
  }

}
