import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { HttpClientModule } from '@angular/common/http';
import { ContributedRepositoriesComponent } from './contributed-repositories/contributed-repositories.component';
import { OwnedRepositoriesComponent } from './owned-repositories/owned-repositories.component';
import { RepositoryService } from './shared/repository.service';
import { NewRepositoryComponent } from './new-repository/new-repository.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule
  ],
  exports: [
    ContributedRepositoriesComponent,
    OwnedRepositoriesComponent,
    NewRepositoryComponent
  ],
  declarations: [
    ContributedRepositoriesComponent,
    OwnedRepositoriesComponent,
    NewRepositoryComponent
  ],
  entryComponents: [NewRepositoryComponent],
  providers: [RepositoryService]
})
export class RepositoryModule { }
