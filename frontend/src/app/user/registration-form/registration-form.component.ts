import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

import { RegistrationUser } from '../shared/registration-user.model';
import { UserService } from '../shared/user.service';

@Component({
  selector: 'it-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.css']
})
export class RegistrationFormComponent implements OnInit {
  signUpForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {

    this.signUpForm = this.formBuilder.group(
      {
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        username: ['', Validators.required],
        password: ['', [Validators.required, Validators.minLength(8)]],
        confirmedPassword: ['', [Validators.required, Validators.minLength(8)]]
      },
      {
        validator: this.checkIfMatchingPasswords()
      }
    );
  }

  get firstName() {
    return this.signUpForm.get('firstName');
  }

  get lastName() {
    return this.signUpForm.get('lastName');
  }

  get email() {
    return this.signUpForm.get('email');
  }

  get username() {
    return this.signUpForm.get('username');
  }

  get password() {
    return this.signUpForm.get('password');
  }

  get confirmedPassword() {
    return this.signUpForm.get('confirmedPassword');
  }

  onRegister() {
    if (this.signUpForm.valid) {
      const user = new RegistrationUser(this.signUpForm.value);
      this.signUpForm.reset();
      this.userService.register(user).subscribe(
        () =>
          this.snackBar.open('You have registered successfully.', 'OK', {
            duration: 2000
          }),
        err => {
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    }
  }

  checkIfMatchingPasswords() {
    return () => {
      if (!this.signUpForm || !this.confirmedPassword) {
        return;
      }
      if (this.password && this.password.value !== this.confirmedPassword.value) {
        return this.confirmedPassword.setErrors({ notEquivalent: true });
      } else {
        return this.confirmedPassword.setErrors(null);
      }
    };
  }
}
