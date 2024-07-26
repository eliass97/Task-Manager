import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { JobTitle } from '../models/type';

@Injectable({
    providedIn: 'root'
})
export class JobTitleService {

    private apiURL = environment.apiBaseUrl + '/job-title';

    constructor(private http: HttpClient) {
    }

    public getAllTitles(): Observable<JobTitle[]> {
        return this.http.get<JobTitle[]>(`${this.apiURL}`);
    }
}
