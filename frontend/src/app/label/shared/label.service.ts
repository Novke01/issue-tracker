import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../environments/environment';
import { Label } from './label.model';

@Injectable()
export class LabelService {
  private labelsUrl = 'api/labels';

  constructor(private http: HttpClient) {}

  createLabel(label: Label): Observable<Label> {
    const url = `${environment.baseUrl}${this.labelsUrl}`;
    return this.http.post<Label>(url, label, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }

  getById(id: number): Observable<Label> {
    const url = `${environment.baseUrl}${this.labelsUrl}/${id}`;
    return this.http.get<Label>(url);
  }

  removeLabel(labelId: number) {
    const url = `${environment.baseUrl}${this.labelsUrl}/${labelId}`;
    return this.http.delete<Label>(url);
  }
}
