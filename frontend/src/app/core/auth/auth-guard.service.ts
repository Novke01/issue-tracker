import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AuthService } from './auth.service';

@Injectable()
export class AuthGuardService implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.shouldPass(state.url);
  }

  shouldPass(url: string) {
    if (this.authService.user) {
      return true;
    } else {
      this.router
        .navigate(["/login"], {
          queryParams: {
            return: url
          }
        })
        .then(result => {
          console.log(result);
        });
      return false;
    }
  }
}
