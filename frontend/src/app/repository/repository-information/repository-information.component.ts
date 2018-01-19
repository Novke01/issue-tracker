import { RepositoryService } from './../shared/repository.service';
import { Repository } from './../shared/repository.model';
import { Component, OnInit, Input, AfterContentInit, ViewChild  } from '@angular/core';
import { User } from '../../core/auth/user.model';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';

@Component({
  selector: 'it-repository-information',
  templateUrl: './repository-information.component.html',
  styleUrls: ['./repository-information.component.css']
})
export class RepositoryInformationComponent implements OnInit  {

  contributors: User[];
  owner: User = new User();

  displayedColumns = ['firstName', 'lastName', 'email', 'owner'];
  dataSource: MatTableDataSource<User>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private repositoryService: RepositoryService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.contributors = new Array<User>();
    this.route.paramMap
    .switchMap((params: ParamMap) =>
      this.repositoryService.getContributorsByRepositoryId(params.get('id'))).subscribe(result => {
      this.contributors = this.contributors.concat(result);
      this.dataSource = new MatTableDataSource(this.contributors);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      });
    this.route.paramMap
    .switchMap((params: ParamMap) =>
      this.repositoryService.getOwnerByRepositoryId(params.get('id'))).subscribe(result => {
      this.owner = result;
      this.contributors.unshift(this.owner);
      this.dataSource = new MatTableDataSource(this.contributors);
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
