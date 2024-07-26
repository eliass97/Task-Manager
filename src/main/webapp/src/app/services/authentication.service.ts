import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginRequest, LoginResponse } from '../models/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private USER_ID = 'user-id';
  private AUTHENTICATION_TOKEN = 'authentication-token';
  private REFRESH_TOKEN = 'refresh-token';
  private TOKEN_EXPIRES_AT = 'token-expires-at';

  private baseURL = environment.baseUrl;

  constructor(
    private http: HttpClient
  ) { }

  public isLoggedIn(): boolean {
    return !!localStorage.getItem(this.AUTHENTICATION_TOKEN);
  }

  public getUserId(): number | null {
    const userId = localStorage.getItem(this.USER_ID);
    return userId ? parseInt(userId) : null;
  }

  public getToken(): string {
    const token = localStorage.getItem(this.AUTHENTICATION_TOKEN);
    return token ? token : '';
  }

  public login(loginRequest: LoginRequest): Observable<boolean> {
    return this.http.post<LoginResponse>(`${this.baseURL}/login`, loginRequest)
      .pipe(map(data => {
        localStorage.setItem(this.USER_ID, data.userId ? data.userId.toString() : '');
        localStorage.setItem(this.AUTHENTICATION_TOKEN, data.token ? data.token : '');
        localStorage.setItem(this.REFRESH_TOKEN, data.refreshToken ? data.refreshToken : '');
        localStorage.setItem(this.TOKEN_EXPIRES_AT, data.expiresAt ? data.expiresAt.toString() : '');
        return true;
      }));
  }

  public refreshToken(): Observable<string> {
    const refreshToken = localStorage.getItem(this.REFRESH_TOKEN);
    const request = new LoginRequest();
    if (refreshToken) {
      request.refreshToken = refreshToken;
    }
    return this.http.post<LoginResponse>(`${this.baseURL}/refreshToken`, request)
      .pipe(map(data => {
        const newToken = data.token ? data.token : '';
        localStorage.setItem(this.AUTHENTICATION_TOKEN, newToken);
        localStorage.setItem(this.TOKEN_EXPIRES_AT, data.expiresAt ? data.expiresAt.toString() : '');
        return newToken;
      }));
  }

  public logout(): void {
    const refreshToken = localStorage.getItem(this.REFRESH_TOKEN);
    if (refreshToken) {
      const request = new LoginRequest();
      request.refreshToken = refreshToken;
      this.http.post(`${this.baseURL}/logout`, request, { observe: 'response', responseType: 'text' }).subscribe(
        (response) => {
          if (response?.url) {
            window.location.href = response.url;
          }
        }
      );
    }
    localStorage.removeItem(this.USER_ID);
    localStorage.removeItem(this.AUTHENTICATION_TOKEN);
    localStorage.removeItem(this.REFRESH_TOKEN);
    localStorage.removeItem(this.TOKEN_EXPIRES_AT);
  }
}
