import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITag } from 'app/shared/model/tag.model';

type EntityResponseType = HttpResponse<ITag>;
type EntityArrayResponseType = HttpResponse<ITag[]>;

@Injectable({ providedIn: 'root' })
export class TagService {
    public resourceUrl = SERVER_API_URL + 'api/tags';

    constructor(protected http: HttpClient) {}

    create(tag: ITag): Observable<EntityResponseType> {
        return this.http.post<ITag>(this.resourceUrl, tag, { observe: 'response' });
    }

    update(tag: ITag): Observable<EntityResponseType> {
        return this.http.put<ITag>(this.resourceUrl, tag, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ITag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITag[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    listByTargetSystem(tsId: string): Observable<HttpResponse<string[]>> {
        return this.http.get<string[]>(`${this.resourceUrl}/target-system/${tsId}`, { observe: 'response' });
    }
}
