import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';

import { CreateMilestoneComponent } from '../create-milestone/create-milestone.component';
import { Milestone } from '../shared/milestone.model';
import { MilestoneService } from './../shared/milestone.service';

@Component({
  selector: 'it-repository-milestones',
  templateUrl: './repository-milestones.component.html',
  styleUrls: ['./repository-milestones.component.css']
})
export class RepositoryMilestonesComponent implements OnInit {
  repositoryId: number;
  milestones: Milestone[] = [];
  displayedColumns = ['title', 'description', 'dueDate', 'actions'];
  dataSource: MatTableDataSource<Milestone>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private milestoneService: MilestoneService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.repositoryId = +this.route.snapshot.paramMap.get('id');
    this.milestoneService
      .getByRepositoryId(this.repositoryId)
      .subscribe(result => {
        this.milestones = result;
        this.dataSource = new MatTableDataSource(this.milestones);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  openCreateMilestoneDialog() {
    const dialogRef = this.dialog.open(CreateMilestoneComponent, {
      hasBackdrop: false
    });

    dialogRef.componentInstance.repositoryId = this.repositoryId;

    dialogRef.afterClosed().subscribe(newMilestone => {
      if (newMilestone !== '') {
        this.milestones.push(newMilestone);
        this.dataSource = new MatTableDataSource(this.milestones);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        while (this.dataSource.paginator.hasNextPage()) {
          this.dataSource.paginator.nextPage();
        }
      }
    });
  }

  remove(id: number) {
    this.milestoneService.remove(id).subscribe(() => {
      this.milestones = this.milestones.filter(function(l) {
        return l.id !== id;
      });
      this.dataSource = new MatTableDataSource(this.milestones);
    });
  }
}
