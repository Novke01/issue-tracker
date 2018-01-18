import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateIssueComponent } from './create-issue/create-issue.component';
import { IssueService } from './shared/issue.service';
import { SharedModule } from '../shared/shared.module';
import { HttpClientModule } from '@angular/common/http';
import { PossibleAssigneesSearchComponent } from './possible-assignees-search/possible-assignees-search.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule
  ],
  declarations: [CreateIssueComponent, PossibleAssigneesSearchComponent],
  providers: [IssueService],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class IssueModule { }
