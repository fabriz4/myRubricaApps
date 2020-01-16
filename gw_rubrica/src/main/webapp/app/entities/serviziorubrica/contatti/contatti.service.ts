import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IContatti } from 'app/shared/model/serviziorubrica/contatti.model';

type EntityResponseType = HttpResponse<IContatti>;
type EntityArrayResponseType = HttpResponse<IContatti[]>;

@Injectable({ providedIn: 'root' })
export class ContattiService {
  public resourceUrl = SERVER_API_URL + 'services/serviziorubrica/api/contattis';

  constructor(protected http: HttpClient) {}

  create(contatti: IContatti): Observable<EntityResponseType> {
    return this.http.post<IContatti>(this.resourceUrl, contatti, { observe: 'response' });
  }

  update(contatti: IContatti): Observable<EntityResponseType> {
    return this.http.put<IContatti>(this.resourceUrl, contatti, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContatti>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContatti[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
