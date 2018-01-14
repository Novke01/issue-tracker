import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { HttpClientModule } from '@angular/common/http';
import { ContributedRepositoriesComponent } from './contributed-repositories/contributed-repositories.component';
import { OwnerRepositoriesComponent } from './owner-repositories/owner-repositories.component';
import { RepositoryService } from './shared/repository.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule
  ],
  exports: [
    ContributedRepositoriesComponent,
    OwnerRepositoriesComponent
  ],
  declarations: [
    ContributedRepositoriesComponent,
    OwnerRepositoriesComponent
  ],
  providers: [RepositoryService]
})
export class RepositoryModule { }
