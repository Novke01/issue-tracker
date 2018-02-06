import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { MatDialog } from '@angular/material';

import { AuthService } from '../../core/auth/auth.service';
import { NewRepositoryComponent } from '../new-repository/new-repository.component';
import { Repository } from '../shared/repository.model';
import { RepositoryService } from '../shared/repository.service';

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

  constructor(
    private authService: AuthService,
    private repositoryService: RepositoryService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    if (this.authService.user) {
      this.repositoryService.getOwnedRepositories(this.authService.user.id).subscribe(data => {
        this.repositories = data;
        this.dataSource = new MatTableDataSource(this.repositories);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      });
    }
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  openNewRepositoryDialog() {
    const dialogRef = this.dialog.open(NewRepositoryComponent, {
      hasBackdrop: false,
      data: { newRepository: this.newRepository }
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
