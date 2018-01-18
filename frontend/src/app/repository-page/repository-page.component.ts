import { Repository } from './../repository/shared/repository.model';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { RepositoryService } from '../repository/shared/repository.service';
import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'it-repository-page',
  templateUrl: './repository-page.component.html',
  styleUrls: ['./repository-page.component.css']
})
export class RepositoryPageComponent implements OnInit {

  repository: Repository = new Repository();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private repositoryService: RepositoryService
  ) { }

  ngOnInit() {
    this.route.paramMap
    .switchMap((params: ParamMap) =>
      this.repositoryService.getRepositoryById(params.get('id'))).subscribe(repository => {
      this.repository = repository;
      });
  }

}
