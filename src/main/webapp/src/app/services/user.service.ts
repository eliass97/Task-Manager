import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { RegistrationRequest } from '../models/auth';
import { UserData } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiURL = environment.apiBaseUrl + '/user';

  constructor(private http: HttpClient) {
  }

  public getAllUsers(): Observable<UserData[]> {
    return this.http.get<UserData[]>(`${this.apiURL}/all`);
  }

  public getUserData(userId: number): Observable<UserData> {
    const httpParams = new HttpParams()
      .set('userId', userId);
    return this.http.get<UserData>(`${this.apiURL}`, { params: httpParams });
  }

  public registerUser(registrationRequest: RegistrationRequest): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/register`, registrationRequest, { responseType: 'text' as 'json' });
  }

  public updateUserProfile(userData: UserData): Observable<UserData> {
    return this.http.put<UserData>(`${this.apiURL}/updateProfile`, userData);
  }

  public updateUserImage(userId: number, image: File): Observable<string> {
    const httpParams = new HttpParams()
      .set('userId', userId);
    const formData = new FormData();
    formData.set('image', image);
    return this.http.put<string>(`${this.apiURL}/updateImage`, formData, { params: httpParams, responseType: 'text' as 'json' });
  }
}
