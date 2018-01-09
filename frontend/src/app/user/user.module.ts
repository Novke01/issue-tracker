import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistrationFormComponent } from './registration-form/registration-form.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { SharedModule } from '../shared/shared.module';
import { HttpClientModule } from '@angular/common/http';
import { UserService } from './shared/user.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule
  ],
  exports: [
    RegistrationFormComponent,
    LoginFormComponent
  ],
  declarations: [
    RegistrationFormComponent,
    LoginFormComponent
  ],
  providers: [UserService]
})
export class UserModule { }
