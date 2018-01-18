import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { HttpClientModule } from '@angular/common/http';
import { ContributedRepositoriesComponent } from './contributed-repositories/contributed-repositories.component';
import { OwnedRepositoriesComponent } from './owned-repositories/owned-repositories.component';
import { RepositoryService } from './shared/repository.service';
import { NewRepositoryComponent } from './new-repository/new-repository.component';
import { AppRoutingModule } from '../app-routing.module';
import { RepositoryInformationComponent } from './repository-information/repository-information.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule,
    AppRoutingModule
  ],
  exports: [
    ContributedRepositoriesComponent,
    OwnedRepositoriesComponent,
    NewRepositoryComponent,
    RepositoryInformationComponent
  ],
  declarations: [
    ContributedRepositoriesComponent,
    OwnedRepositoriesComponent,
    NewRepositoryComponent,
    RepositoryInformationComponent
  ],
  entryComponents: [NewRepositoryComponent],
  providers: [RepositoryService]
})
export class RepositoryModule { }
