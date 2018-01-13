import { Component } from '@angular/core';

@Component({
  selector: 'it-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  // displayedColumns = ['name', 'url', 'description'];
  // repositories: Repository[] = [];
  // dataSource: MatTableDataSource<Repository> = new MatTableDataSource(this.repositories);

  // @ViewChild(MatPaginator) paginator: MatPaginator;
  // @ViewChild(MatSort) sort: MatSort;

  // constructor(private repositoryService: RepositoryService) {}

  // ngOnInit() {
  //   this.repositoryService.getOwnedRepositories().subscribe(data => {
  //     this.repositories = data;
  //     this.dataSource = new MatTableDataSource(this.repositories);
  //   });
  // }

  // ngAfterViewInit() {
  //   this.dataSource.paginator = this.paginator;
  //   this.dataSource.sort = this.sort;
  // }

  // applyFilter(filterValue: string) {
  //   filterValue = filterValue.trim(); // Remove whitespace
  //   filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
  //   this.dataSource.filter = filterValue;
  // }
}
