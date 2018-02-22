import 'rxjs/add/operator/switchMap';

import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';

import { User } from '../../core/auth/user.model';
import { UserService } from '../../user/shared/user.service';
import { RepositorySave } from '../shared/repository-save.model';
import { Repository } from '../shared/repository.model';
import { RepositoryService } from '../shared/repository.service';

@Component({
  selector: 'it-repository-information',
  templateUrl: './repository-information.component.html',
  styleUrls: ['./repository-information.component.css']
})
export class RepositoryInformationComponent implements OnInit {
  form: FormGroup;
  control: FormControl = new FormControl();

  contributors: User[];
  owner: User = new User();

  repository: Repository;

  possibleContributors: User[];

  updateState: Boolean = false;

  displayedColumns = ['firstName', 'lastName', 'email', 'owner'];
  dataSource: MatTableDataSource<User>;
  displayedColumnsRepo = ['name', 'description', 'url'];
  dataSourceRepo: MatTableDataSource<Repository>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private repositoryService: RepositoryService,
    private userService: UserService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private router: Router
  ) {}

  ngOnInit() {
    this.contributors = [];
    this.route.paramMap
      .switchMap((params: ParamMap) =>
        this.repositoryService.getContributorsByRepositoryId(params.get('id'))
      )
      .subscribe(result => {
        this.contributors = this.contributors.concat(result);
        this.dataSource = new MatTableDataSource(this.contributors);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      });
    this.route.paramMap
      .switchMap((params: ParamMap) =>
        this.repositoryService.getOwnerByRepositoryId(params.get('id'))
      )
      .subscribe(result => {
        this.owner = result;
        this.contributors.unshift(this.owner);
        this.dataSource = new MatTableDataSource(this.contributors);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      });

    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      url: ['', Validators.required],
      description: ['', Validators.required]
    });

    this.route.paramMap
      .switchMap((params: ParamMap) =>
        this.repositoryService.getRepositoryById(params.get('id'))
      )
      .subscribe(result => {
        this.repository = result;
        this.dataSourceRepo = new MatTableDataSource([this.repository]);
        this.name.setValue(result.name);
        this.url.setValue(result.url);
        this.description.setValue(result.description);
      });

    this.userService.getAll().subscribe(data => {
      this.possibleContributors = data.filter(
        user =>
          this.contributors.findIndex(
            contributor => contributor.id === user.id
          ) === -1
      );
    });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  submit(form) {
    if (form.valid) {
      const repository = new RepositorySave();

      repository.id = this.repository.id;
      repository.name = form.value.name;
      repository.url = form.value.url;
      repository.description = form.value.description;
      repository.ownerId = this.repository.ownerId;
      repository.contributors = [];
      if (this.control.value !== null) {
        // Gather current contributors.
        repository.contributors = this.control.value.map(
          contributor => contributor.id
        );
      }
      // Add previous contributors.
      repository.contributors = repository.contributors.concat(
        this.contributors.map(contributor => contributor.id)
      );

      this.repositoryService.updateRepository(repository).subscribe(() => {
        this.toggleUpdateState();
        this.ngOnInit();
      });
    }
  }

  remove(id: number) {
    this.repositoryService.remove(id).subscribe(() => {
      this.router.navigate(['/']);
    });
  }

  get name() {
    return this.form.get('name');
  }

  get url() {
    return this.form.get('url');
  }

  get description() {
    return this.form.get('description');
  }

  toggleUpdateState() {
    this.updateState = !this.updateState;
  }

  getUpdateState() {
    return this.updateState;
  }
}
