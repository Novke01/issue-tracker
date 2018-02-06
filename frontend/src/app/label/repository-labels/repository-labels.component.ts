import { Component, OnInit, ViewChild } from '@angular/core';
import { Label } from '../shared/label.model';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { MatDialog } from '@angular/material';
import { RepositoryService } from '../../repository/shared/repository.service';
import { ActivatedRoute } from '@angular/router';
import { CreateLabelComponent } from '../create-label/create-label.component';
import { LabelService } from '../shared/label.service';
import { NumberValueAccessor } from '@angular/forms/src/directives/number_value_accessor';

@Component({
  selector: 'it-repository-labels',
  templateUrl: './repository-labels.component.html',
  styleUrls: ['./repository-labels.component.css']
})
export class RepositoryLabelsComponent implements OnInit {
  displayedColumns = ['name', 'actions'];
  labels: Label[];
  dataSource: MatTableDataSource<Label>;
  repositoryId: number;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private labelService: LabelService,
    private repositoryService: RepositoryService,
    private dialog: MatDialog,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.repositoryId = +this.route.snapshot.paramMap.get('id');

    this.repositoryService
      .getLabelsByRepositoryId(this.repositoryId)
      .subscribe(result => {
        this.labels = result;
        this.dataSource = new MatTableDataSource(this.labels);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  remove(labelId: number) {
    this.labelService.removeLabel(labelId).subscribe(_ => {
      this.labels = this.labels.filter(function(l) {
        return l.id !== labelId;
      });
      this.dataSource = new MatTableDataSource(this.labels);
    });
  }

  openCreateLabelDialog() {
    const dialogRef = this.dialog.open(CreateLabelComponent, {
      hasBackdrop: false
    });

    dialogRef.componentInstance.repositoryId = this.repositoryId;

    dialogRef.afterClosed().subscribe(newLabel => {
      if (newLabel !== '') {
        this.labels.push(newLabel);
        this.dataSource = new MatTableDataSource(this.labels);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        while (this.dataSource.paginator.hasNextPage()) {
          this.dataSource.paginator.nextPage();
        }
      }
    });
  }
}
