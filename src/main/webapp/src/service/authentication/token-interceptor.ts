import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, catchError, filter, switchMap, take, throwError } from "rxjs";
import { LoginResponse } from "../../model/auth";
import { AuthenticationService } from "./authentication.service";

@Injectable({
    providedIn: "root"
})
export class TokenInterceptor implements HttpInterceptor {

    isTokenRefreshing = false;
    refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);

    constructor(
        private authenticationService: AuthenticationService
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (req.url.indexOf('refresh') !== -1 || req.url.indexOf('login') !== -1) {
            return next.handle(req);
        }

        const token = localStorage.getItem('authentication-token');
        if (token) {
            req.clone({
                headers: req.headers.set('Authorization', 'Bearer ' + token)
            });

            return next.handle(req).pipe(catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === 403) {
                    return this.handleAuthErrors(req, next);
                } else {
                    return throwError(error);
                }
            }));
        }
        return next.handle(req);
    }

    private handleAuthErrors(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.isTokenRefreshing) {
            this.isTokenRefreshing = true;
            this.refreshTokenSubject.next(null);

            return this.authenticationService.refreshToken().pipe(
                switchMap((response: LoginResponse) => {
                    this.isTokenRefreshing = false;
                    this.refreshTokenSubject.next(response.refreshToken);
                    req.clone({
                        headers: req.headers.set('Authorization', 'Bearer ' + response.token)
                    });

                    return next.handle(req);
                })
            )
        } else {
            return this.refreshTokenSubject.pipe(
                filter(result => result !== null),
                take(1),
                switchMap((res) => {
                    const token = localStorage.getItem('authentication-token');
                    req.clone({
                        headers: req.headers.set('Authorization', 'Bearer ' + token)
                    });
                    return next.handle(req);
                })
            );
        }
    }
}
