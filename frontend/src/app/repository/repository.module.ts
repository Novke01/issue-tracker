import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { HttpClientModule } from '@angular/common/http';
import { ContributedRepositoriesComponent } from './contributed-repositories/contributed-repositories.component';
import { OwnedRepositoriesComponent } from './owned-repositories/owned-repositories.component';
import { RepositoryService } from './shared/repository.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule
  ],
  exports: [
    ContributedRepositoriesComponent,
    OwnedRepositoriesComponent
  ],
  declarations: [
    ContributedRepositoriesComponent,
    OwnedRepositoriesComponent
  ],
  providers: [RepositoryService]
})
export class RepositoryModule { }
