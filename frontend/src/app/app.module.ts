import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

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




@NgModule({
  declarations: [
    AppComponent,
    StarterPageComponent,
    HomePageComponent,
  ],
  imports: [
    BrowserModule,
    CoreModule,
    MatCardModule,
    MatTabsModule,
    UserModule,
    AppRoutingModule,
    SharedModule,
    RepositoryModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
