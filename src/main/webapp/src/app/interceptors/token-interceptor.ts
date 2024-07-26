import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, filter, switchMap, take, throwError } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';

@Injectable({
    providedIn: 'root'
})
export class TokenInterceptor implements HttpInterceptor {

    private isTokenRefreshing = false;
    private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);

    constructor(
        private authenticationService: AuthenticationService
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (req.url.indexOf('refresh') !== -1 || req.url.indexOf('login') !== -1) {
            return next.handle(req);
        }

        const token = this.authenticationService.getToken();
        if (token) {
            req = this.updateBearer(req, token);

            return next.handle(req).pipe(catchError(error => {
                return (error instanceof HttpErrorResponse && error.status === 401)
                    ? this.handleAuthErrors(req, next)
                    : throwError(() => error);
            }));
        }

        return next.handle(req);
    }

    private updateBearer(req: HttpRequest<any>, token: string): HttpRequest<any> {
        return req.clone({
            headers: req.headers.set('Authorization', 'Bearer ' + token)
        });
    }

    private handleAuthErrors(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.isTokenRefreshing) {
            this.isTokenRefreshing = true;
            this.refreshTokenSubject.next(null);

            return this.authenticationService.refreshToken().pipe(
                switchMap((token: string) => {
                    this.isTokenRefreshing = false;
                    this.refreshTokenSubject.next(token);

                    req = this.updateBearer(req, token);

                    return next.handle(req);
                })
            )
        } else {
            return this.refreshTokenSubject.pipe(
                filter(result => result !== null),
                take(1),
                switchMap(() => {
                    const token = this.authenticationService.getToken();
                    req = this.updateBearer(req, token);

                    return next.handle(req);
                })
            );
        }
    }
}
