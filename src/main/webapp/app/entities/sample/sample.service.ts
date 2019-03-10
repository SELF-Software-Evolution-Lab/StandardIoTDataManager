import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISample } from 'app/shared/model/sample.model';

type EntityResponseType = HttpResponse<ISample>;
type EntityArrayResponseType = HttpResponse<ISample[]>;

@Injectable({ providedIn: 'root' })
export class SampleService {
    public resourceUrl = SERVER_API_URL + 'api/samples';

    constructor(protected http: HttpClient) {}

    create(sample: ISample): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sample);
        return this.http
            .post<ISample>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sample: ISample): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sample);
        return this.http
            .put<ISample>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ISample>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISample[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(sample: ISample): ISample {
        const copy: ISample = Object.assign({}, sample, {
            ts: sample.ts != null && sample.ts.isValid() ? sample.ts.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.ts = res.body.ts != null ? moment(res.body.ts) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((sample: ISample) => {
                sample.ts = sample.ts != null ? moment(sample.ts) : null;
            });
        }
        return res;
    }
}
