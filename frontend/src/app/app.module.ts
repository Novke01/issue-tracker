import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MarkdownModule } from 'ngx-md';

import { AppComponent } from './app.component';
import { UserModule } from './user/user.module';
import { StarterPageComponent } from './starter-page/starter-page.component';
import { SharedModule } from './shared/shared.module';
import { CoreModule } from './core/core.module';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { AppRoutingModule } from './app-routing.module';
import { HomePageComponent } from './home-page/home-page.component';
import { RepositoryModule } from './repository/repository.module';
import { RepositoryPageComponent } from './repository-page/repository-page.component';
import { IssueModule } from './issue/issue.module';
import { WikiModule } from './wiki/wiki.module';




@NgModule({
  declarations: [
    AppComponent,
    StarterPageComponent,
    HomePageComponent,
    RepositoryPageComponent,
  ],
  imports: [
    BrowserModule,
    CoreModule,
    MatCardModule,
    MatTabsModule,
    UserModule,
    AppRoutingModule,
    SharedModule,
    RepositoryModule,
    IssueModule,
    WikiModule,
    MarkdownModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
