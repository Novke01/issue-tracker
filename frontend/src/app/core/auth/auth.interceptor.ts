import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/finally';
import 'rxjs/add/operator/switchMap';

import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { filter, switchMap, take } from 'rxjs/operators';

import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshingToken = false;
  private tokenSubject = new BehaviorSubject<string>(null);
  private authService: AuthService;

  constructor(private injector: Injector) {}

  private addToken(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  private refreshToken(request: HttpRequest<any>, next: HttpHandler) {
    if (this.isRefreshingToken) {
      return this.tokenSubject.pipe(
        filter(token => token != null),
        take(1),
        switchMap(token => {
          return next.handle(this.addToken(request, token));
        })
      );
    } else {
      this.isRefreshingToken = true;
      this.tokenSubject.next(null);

      return this.authService.refreshToken().switchMap((tokens, _) => {
        if (tokens) {
          this.tokenSubject.next(tokens.accessToken);
          return next.handle(this.addToken(request, tokens.accessToken));
        }
        return this.authService.logout();
      })
      .catch(error => this.authService.logout())
      .finally(() => (this.isRefreshingToken = false));
    }
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.method === 'OPTION' ||
        (request.url.indexOf('/api/auth/login') !== -1 &&
        request.url.indexOf('/api/user/registration') !== -1)) {
      return next.handle(request);
    }
    this.authService = this.injector.get(AuthService);
    const token = localStorage.getItem('access_token');
    request = this.addToken(request, token);
    return next.handle(request).catch(event => {
      if (event instanceof HttpErrorResponse && event.status === 401) {
        return this.refreshToken(request, next);
      } else {
        return Observable.throw(event);
      }
    });
  }
}
