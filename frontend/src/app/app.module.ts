import { NgModule } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { BrowserModule } from '@angular/platform-browser';
import { MarkdownModule } from 'ngx-md';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { HomePageComponent } from './home-page/home-page.component';
import { IssueModule } from './issue/issue.module';
import { LabelModule } from './label/label.module';
import { MilestoneModule } from './milestone/milestone.module';
import { RepositoryPageComponent } from './repository-page/repository-page.component';
import { RepositoryModule } from './repository/repository.module';
import { SharedModule } from './shared/shared.module';
import { StarterPageComponent } from './starter-page/starter-page.component';
import { UserModule } from './user/user.module';
import { WikiModule } from './wiki/wiki.module';

@NgModule({
  declarations: [
    AppComponent,
    StarterPageComponent,
    HomePageComponent,
    RepositoryPageComponent
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
    LabelModule,
    MilestoneModule,
    MarkdownModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
