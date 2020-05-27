import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISubSet } from 'app/shared/model/sub-set.model';

type EntityResponseType = HttpResponse<ISubSet>;
type EntityArrayResponseType = HttpResponse<ISubSet[]>;

@Injectable({ providedIn: 'root' })
export class SubSetService {
    public resourceUrl = SERVER_API_URL + 'api/sub-sets';

    constructor(protected http: HttpClient) {}

    create(subSet: ISubSet): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(subSet);
        return this.http
            .post<ISubSet>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(subSet: ISubSet): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(subSet);
        return this.http
            .put<ISubSet>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ISubSet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISubSet[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(subSet: ISubSet): ISubSet {
        const copy: ISubSet = Object.assign({}, subSet, {
            dateCreated: subSet.dateCreated != null && subSet.dateCreated.isValid() ? subSet.dateCreated.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((subSet: ISubSet) => {
                subSet.dateCreated = subSet.dateCreated != null ? moment(subSet.dateCreated) : null;
            });
        }
        return res;
    }
}
