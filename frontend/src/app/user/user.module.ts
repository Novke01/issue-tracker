import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared.module';
import { LoginFormComponent } from './login-form/login-form.component';
import { RegistrationFormComponent } from './registration-form/registration-form.component';
import { UserService } from './shared/user.service';
import { ProfileComponent } from './profile/profile.component';
import { RepositoryModule } from '../repository/repository.module';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule,
    RepositoryModule
  ],
  exports: [
    RegistrationFormComponent,
    LoginFormComponent
  ],
  declarations: [
    RegistrationFormComponent,
    LoginFormComponent,
    ProfileComponent
  ],
  providers: [UserService]
})
export class UserModule {}
