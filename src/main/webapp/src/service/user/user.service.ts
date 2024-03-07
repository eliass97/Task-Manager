import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";
import { UserData } from "../../model/user";
import { RegistrationRequest } from "../../model/auth";

@Injectable({
  providedIn: "root"
})
export class UserService {

  private apiURL = environment.apiBaseUrl + "/user";

  constructor(private http: HttpClient) {
  }

  public registerUser(registrationRequest: RegistrationRequest): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/register`, registrationRequest);
  }

  public activateUser(token: string): Observable<string> {
    const httpParams = new HttpParams();
    httpParams.append("token", token);
    const options = { params: httpParams };
    return this.http.get<string>(`${this.apiURL}/activate`, options);
  }

  public updateUserProfile(userData: UserData): Observable<UserData> {
    return this.http.put<UserData>(`${this.apiURL}/update`, userData);
  }
}
