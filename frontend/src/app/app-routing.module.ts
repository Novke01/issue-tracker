import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {AuthGuardService} from './core/auth/auth-guard.service';
import {HomePageComponent} from './home-page/home-page.component';
import {IssueDisplayComponent} from './issue/issue-display/issue-display.component';
import {DisplayPullRequestComponent} from './pull-request/display-pull-request/display-pull-request.component';
import {RepositoryPageComponent} from './repository-page/repository-page.component';
import {StarterPageComponent} from './starter-page/starter-page.component';
import {ProfileComponent} from './user/profile/profile.component';
import {NewWikiComponent} from './wiki/new-wiki/new-wiki.component';

const appRoutes: Routes = [
    {path: 'login', component: StarterPageComponent},
    {
        path: 'repository/:id',
        component: RepositoryPageComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'repositories/:repoId/issues/:issueId',
        component: IssueDisplayComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'repository/:id/wiki/new',
        component: NewWikiComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'repository/:id/wiki/:wikiId',
        component: NewWikiComponent,
        canActivate: [AuthGuardService]
    },
    {path: '', component: HomePageComponent, canActivate: [AuthGuardService]},
    {
        path: 'profile/:id',
        component: ProfileComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'repository/:repoId/pull-request/:pullRequestId',
        component: DisplayPullRequestComponent,
        canActivate: [AuthGuardService]
    }
];

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
