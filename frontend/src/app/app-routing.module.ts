import { RepositoryPageComponent } from './repository-page/repository-page.component';
import { NgModule } from '@angular/core';
import { Routes } from '@angular/router';

import { StarterPageComponent } from './starter-page/starter-page.component';
import { HomePageComponent } from './home-page/home-page.component';
import { AuthGuardService } from './core/auth/auth-guard.service';
import { RouterModule } from '@angular/router';
import { environment } from '../environments/environment';
import { IssueDisplayComponent } from './issue/issue-display/issue-display.component';


const appRoutes: Routes = [
  { path: 'login', component: StarterPageComponent },
  { path: 'repository/:id', component: RepositoryPageComponent, canActivate: [AuthGuardService] },
  { path: 'repositories/:repoId/issues/:issueId', component: IssueDisplayComponent, canActivate: [AuthGuardService] },
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