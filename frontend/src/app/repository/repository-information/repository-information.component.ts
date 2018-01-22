import {RepositoryService} from './../shared/repository.service';
import {Repository} from './../shared/repository.model';
import {Component, OnInit, Input, AfterContentInit, ViewChild} from '@angular/core';
import {User} from '../../core/auth/user.model';
import {Router, ActivatedRoute, ParamMap} from '@angular/router';
import 'rxjs/add/operator/switchMap';
import {MatTableDataSource, MatPaginator, MatSort} from '@angular/material';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {RepositorySave} from '../shared/repository-save.model';

@Component({
    selector: 'it-repository-information',
    templateUrl: './repository-information.component.html',
    styleUrls: ['./repository-information.component.css']
})
export class RepositoryInformationComponent implements OnInit {

    form: FormGroup;
    control: FormControl = new FormControl();
    repository: RepositorySave;

    contributors: User[];
    owner: User = new User();

    displayedColumns = ['firstName', 'lastName', 'email', 'owner'];
    dataSource: MatTableDataSource<User>;

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

    constructor(private repositoryService: RepositoryService, private route: ActivatedRoute, private formBuilder: FormBuilder) {
    }

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

        this.form = this.formBuilder.group({
            name: ['', Validators.required],
            url: ['', Validators.required],
            description: ['', Validators.required]
        });

        this.route.paramMap
            .switchMap((params: ParamMap) =>
                this.repositoryService.getRepositoryById(params.get('id'))).subscribe(result => {
            this.repository = result;
            this.name.setValue(result.name);
            this.url.setValue(result.url);
            this.description.setValue(result.description);
        });
    }

    applyFilter(filterValue: string) {
        filterValue = filterValue.trim(); // Remove whitespace
        filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
        this.dataSource.filter = filterValue;
    }

    submit(form) {
        if (form.valid) {
            this.repository.name = form.value.name;
            this.repository.url = form.value.url;
            this.repository.description = form.value.description;

            this.repositoryService.updateRepository(this.repository).subscribe(repository => {
                }
            );
        }
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
}
