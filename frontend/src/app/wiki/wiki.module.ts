import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MarkdownModule } from 'ngx-md';

import { AppRoutingModule } from '../app-routing.module';
import { SharedModule } from '../shared/shared.module';
import { NewWikiComponent } from './new-wiki/new-wiki.component';
import { RepositoryWikiComponent } from './repository-wiki/repository-wiki.component';
import { WikiPageService } from './shared/wiki-page.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule,
    AppRoutingModule,
    MarkdownModule,
    FormsModule
  ],
  exports: [RepositoryWikiComponent, NewWikiComponent],
  declarations: [RepositoryWikiComponent, NewWikiComponent],
  entryComponents: [],
  providers: [WikiPageService]
})
export class WikiModule {}
