import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from '../app-routing.module';
import { SharedModule } from '../shared/shared.module';
import { CommentModule } from './../comment/comment.module';
import { CreatePullRequestComponent } from './create-pull-request/create-pull-request.component';
import { DisplayPullRequestComponent } from './display-pull-request/display-pull-request.component';
import { RepositoryPullRequestsComponent } from './repository-pull-requests/repository-pull-requests.component';
import { PullRequestService } from './shared/pull-request.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule,
    AppRoutingModule,
    CommentModule
  ],
  exports: [RepositoryPullRequestsComponent],
  declarations: [
    RepositoryPullRequestsComponent,
    CreatePullRequestComponent,
    DisplayPullRequestComponent
  ],
  providers: [PullRequestService],
  entryComponents: [CreatePullRequestComponent]
})
export class PullRequestModule {}
