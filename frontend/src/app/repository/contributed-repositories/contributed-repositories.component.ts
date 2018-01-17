import { SharedModule } from '../../shared/shared.module';
import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../user/shared/user.service';
import { Repository } from '../shared/repository.model';

import {MatTableDataSource, MatPaginator, MatSort} from '@angular/material';
import { RepositoryService } from '../shared/repository.service';

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

  constructor(private repositoryService: RepositoryService) {}

  ngOnInit() {
    this.repositoryService.getContributedRepositories().subscribe(data => {
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
}
