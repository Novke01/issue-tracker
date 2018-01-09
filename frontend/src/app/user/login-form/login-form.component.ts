import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../core/auth/auth.service';
import { LoginUser } from '../../core/auth/login-user.model';
import { UserService } from '../shared/user.service';

@Component({
  selector: 'it-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

  signInForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.signInForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  get username() { return this.signInForm.get('username'); }
  get password() { return this.signInForm.get('password'); }

  onLogin() {
    if (this.signInForm.valid) {
      const user = new LoginUser(this.signInForm.value);
      this.authService.login(user).subscribe(
        user => this.snackBar.open('You are logged in.', 'OK', {
          duration: 2000
        }),
        err => {
          console.log(err);
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    }
  }

  test() {
    this.userService.getHome().subscribe(result => console.log(result))
  }

}
