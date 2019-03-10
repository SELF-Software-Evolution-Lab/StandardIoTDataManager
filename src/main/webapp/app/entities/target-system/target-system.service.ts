import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITargetSystem } from 'app/shared/model/target-system.model';

type EntityResponseType = HttpResponse<ITargetSystem>;
type EntityArrayResponseType = HttpResponse<ITargetSystem[]>;

@Injectable({ providedIn: 'root' })
export class TargetSystemService {
    public resourceUrl = SERVER_API_URL + 'api/target-systems';

    constructor(protected http: HttpClient) {}

    create(targetSystem: ITargetSystem): Observable<EntityResponseType> {
        return this.http.post<ITargetSystem>(this.resourceUrl, targetSystem, { observe: 'response' });
    }

    update(targetSystem: ITargetSystem): Observable<EntityResponseType> {
        return this.http.put<ITargetSystem>(this.resourceUrl, targetSystem, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ITargetSystem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITargetSystem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
