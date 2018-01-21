import { RepositoryPageComponent } from './repository-page/repository-page.component';
import { NgModule } from '@angular/core';
import { Routes } from '@angular/router';

import { StarterPageComponent } from './starter-page/starter-page.component';
import { HomePageComponent } from './home-page/home-page.component';
import { AuthGuardService } from './core/auth/auth-guard.service';
import { RouterModule } from '@angular/router';
import { environment } from '../environments/environment';
import { CreateIssueComponent } from './issue/create-issue/create-issue.component';
import { NewWikiComponent } from './wiki/new-wiki/new-wiki.component';


const appRoutes: Routes = [
  { path: 'login', component: StarterPageComponent },
  { path: 'repository/:id', component: RepositoryPageComponent, canActivate: [AuthGuardService] },
  { path: 'repository/:id/wiki/new', component: NewWikiComponent, canActivate: [AuthGuardService] },
  { path: '', component: HomePageComponent, canActivate: [AuthGuardService] }
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: false }
    )
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }