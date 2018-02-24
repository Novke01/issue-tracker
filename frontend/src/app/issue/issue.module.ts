import { CommentModule } from './../comment/comment.module';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from '../app-routing.module';
import { SharedModule } from '../shared/shared.module';
import { CreateIssueComponent } from './create-issue/create-issue.component';
import { IssueDisplayComponent } from './issue-display/issue-display.component';
import { PossibleAssigneesSearchComponent } from './possible-assignees-search/possible-assignees-search.component';
import { RepositoryIssuesComponent } from './repository-issues/repository-issues.component';
import { IssueService } from './shared/issue.service';
import { FlexLayoutModule } from '@angular/flex-layout';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    FlexLayoutModule,
    CommentModule
  ],
  exports: [
    CreateIssueComponent,
    PossibleAssigneesSearchComponent,
    RepositoryIssuesComponent,
    IssueDisplayComponent
  ],
  declarations: [
    CreateIssueComponent,
    PossibleAssigneesSearchComponent,
    RepositoryIssuesComponent,
    IssueDisplayComponent
  ],
  providers: [IssueService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IssueModule {}
