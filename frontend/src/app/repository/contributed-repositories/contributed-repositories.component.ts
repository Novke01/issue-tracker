import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';

import { Repository } from '../shared/repository.model';
import { RepositoryService } from '../shared/repository.service';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'it-contributed-repositories',
  templateUrl: './contributed-repositories.component.html',
  styleUrls: ['./contributed-repositories.component.css']
})
export class ContributedRepositoriesComponent implements OnInit {
  displayedColumns = ['name', 'url', 'description'];
  repositories: Repository[];
  dataSource: MatTableDataSource<Repository>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private authService: AuthService,
    private repositoryService: RepositoryService
  ) {}

  ngOnInit() {
    if (this.authService.user) {
      this.repositoryService.getContributedRepositories(this.authService.user.id).subscribe(data => {
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
}
