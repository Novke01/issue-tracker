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


@NgModule({
  declarations: [
    AppComponent,
    StarterPageComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    CoreModule,
    MatCardModule,
    MatTabsModule,
    UserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
