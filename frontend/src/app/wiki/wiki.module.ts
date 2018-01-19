import { RepositoryWikiComponent } from './repository-wiki/repository-wiki.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from '../app-routing.module';
import { RepositoryIssuesComponent } from '../issue/repository-issues/repository-issues.component';
import { CreateIssueComponent } from '../issue/create-issue/create-issue.component';
import { WikiPageService } from './shared/wiki-page.service';
import { MarkdownModule } from 'ngx-md';



@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule,
    AppRoutingModule,
    MarkdownModule
  ],
  exports: [
    RepositoryWikiComponent
  ],
  declarations: [
    RepositoryWikiComponent
  ],
  entryComponents: [],
  providers: [WikiPageService]
})
export class WikiModule { }
