import { NgModule } from '@angular/core';

import { RegistrationFormComponent } from './registration-form/registration-form.component';
import { SharedModule } from '../shared/shared.module';
import { RegistrationService } from './shared/registration.service';

@NgModule({
  imports: [
    SharedModule
  ],
  exports: [
    RegistrationFormComponent
  ],
  declarations: [
    RegistrationFormComponent
  ],
  providers: [RegistrationService]
})
export class RegistrationModule { }
