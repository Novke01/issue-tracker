import { Component } from '@angular/core';

import { AuthService } from './core/auth/auth.service';

@Component({
  selector: 'it-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private authService: AuthService) {}

  user() {
    return this.authService.user;
  }

  logout() {
    this.authService.logout();
  }
}
