import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, map } from "rxjs";
import { environment } from "../../environments/environment";
import { LoginRequest, LoginResponse } from "../../model/auth";

@Injectable({
  providedIn: "root"
})
export class AuthenticationService {

  private baseURL = environment.baseUrl;

  constructor(private http: HttpClient) { }

  public isLoggedIn(): boolean {
    return !!localStorage.getItem('authentication-token');
  }

  public login(loginRequest: LoginRequest): Observable<boolean> {
    return this.http.post<LoginResponse>(`${this.baseURL}/login`, loginRequest).pipe(map(data => {
      localStorage.setItem('authentication-token', data.token ? data.token : '');
      localStorage.setItem('refresh-token', data.refreshToken ? data.refreshToken : '');
      localStorage.setItem('token-expires-at', data.expiresAt ? data.expiresAt.toString() : '');
      return true;
    }));
  }

  public refreshToken(): Observable<LoginResponse> {
    const refreshToken = localStorage.getItem('refresh-token');
    const request = new LoginRequest();
    if (refreshToken) {
      request.refreshToken = refreshToken;
    }
    return this.http.post<LoginResponse>(`${this.baseURL}/refreshToken`, request);
  }

  public logout(): void {
    const refreshToken = localStorage.getItem('refresh-token');
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
    localStorage.removeItem('authentication-token');
    localStorage.removeItem('refresh-token');
    localStorage.removeItem('token-expires-at');
  }
}
