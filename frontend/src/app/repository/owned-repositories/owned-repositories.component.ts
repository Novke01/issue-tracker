import { SharedModule } from '../../shared/shared.module';
import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { UserService } from '../../user/shared/user.service';
import { Repository } from '../shared/repository.model';

import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';
import { RepositoryService } from '../shared/repository.service';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormGroup, FormBuilder } from '@angular/forms';
import { NewRepositoryComponent } from '../new-repository/new-repository.component';

@Component({
  selector: 'it-owned-repositories',
  templateUrl: './owned-repositories.component.html',
  styleUrls: ['./owned-repositories.component.css']
})
export class OwnedRepositoriesComponent implements OnInit {

  displayedColumns = ['name', 'url', 'description'];
  repositories: Repository[];
  dataSource: MatTableDataSource<Repository>;
  newRepository: Repository = new Repository();

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private repositoryService: RepositoryService, private dialog: MatDialog) {}

  ngOnInit() {
    this.repositoryService.getOwnedRepositories().subscribe(data => {
      this.repositories = data;
      this.dataSource = new MatTableDataSource(this.repositories);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  openNewRepositoryDialog() {
    const dialogRef = this.dialog.open(NewRepositoryComponent, {
      hasBackdrop: false,
      data: { newRepository: this.newRepository}
    });

    dialogRef.afterClosed().subscribe(repository => {
      if (repository === null) {
        return;
      }
      this.newRepository = repository;
      this.repositories.push(this.newRepository);
      this.newRepository = new Repository();
      this.dataSource = new MatTableDataSource(this.repositories);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      while (this.dataSource.paginator.hasNextPage()) {
        this.dataSource.paginator.nextPage();
      }
    });
  }
}
