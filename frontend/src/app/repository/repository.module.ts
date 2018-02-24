import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from '../app-routing.module';
import { CreateIssueComponent } from '../issue/create-issue/create-issue.component';
import { RepositoryIssuesComponent } from '../issue/repository-issues/repository-issues.component';
import { CreateLabelComponent } from '../label/create-label/create-label.component';
import { CreateMilestoneComponent } from '../milestone/create-milestone/create-milestone.component';
import { SharedModule } from '../shared/shared.module';
import { ContributedRepositoriesComponent } from './contributed-repositories/contributed-repositories.component';
import { NewRepositoryComponent } from './new-repository/new-repository.component';
import { OwnedRepositoriesComponent } from './owned-repositories/owned-repositories.component';
import { RepositoryInformationComponent } from './repository-information/repository-information.component';
import { RepositoryService } from './shared/repository.service';

@NgModule({
  imports: [CommonModule, SharedModule, HttpClientModule, AppRoutingModule],
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
  entryComponents: [
    NewRepositoryComponent,
    RepositoryIssuesComponent,
    CreateIssueComponent,
    CreateLabelComponent,
    CreateMilestoneComponent
  ],
  providers: [RepositoryService]
})
export class RepositoryModule {}
