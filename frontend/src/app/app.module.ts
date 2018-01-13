import { OwnerRepositoriesComponent } from './home/owner-repositories/owner-repositories.component';
import { RepositoryService } from './repository/repository.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { UserModule } from './user/user.module';
import { StarterPageComponent } from './starter-page/starter-page.component';
import { SharedModule } from './shared/shared.module';
import { CoreModule } from './core/core.module';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { HomeComponent } from './home/home.component';
import { AppRoutingModule } from './app-routing.module';
import { ContributedRepositoriesComponent } from './home/contributed-repositories/contributed-repositories.component';


@NgModule({
  declarations: [
    AppComponent,
    StarterPageComponent,
    HomeComponent,
    OwnerRepositoriesComponent,
    ContributedRepositoriesComponent
  ],
  imports: [
    BrowserModule,
    CoreModule,
    MatCardModule,
    MatTabsModule,
    UserModule,
    AppRoutingModule,
    SharedModule
  ],
  providers: [RepositoryService],
  bootstrap: [AppComponent]
})
export class AppModule { }
