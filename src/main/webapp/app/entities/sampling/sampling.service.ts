import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISampling } from 'app/shared/model/sampling.model';
import { ISensor } from 'app/shared/model/sensor.model';

type EntityResponseType = HttpResponse<ISampling>;
type EntityArrayResponseType = HttpResponse<ISampling[]>;

@Injectable({ providedIn: 'root' })
export class SamplingService {
    public resourceUrl = SERVER_API_URL + 'api/samplings';

    constructor(protected http: HttpClient) {}

    create(sampling: ISampling): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sampling);
        return this.http
            .post<ISampling>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sampling: ISampling): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sampling);
        return this.http
            .put<ISampling>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ISampling>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISampling[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(sampling: ISampling): ISampling {
        const copy: ISampling = Object.assign({}, sampling, {
            startTime: sampling.startTime != null && sampling.startTime.isValid() ? sampling.startTime.toJSON() : null,
            endTime: sampling.endTime != null && sampling.endTime.isValid() ? sampling.endTime.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.startTime = res.body.startTime != null ? moment(res.body.startTime) : null;
            res.body.endTime = res.body.endTime != null ? moment(res.body.endTime) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((sampling: ISampling) => {
                sampling.startTime = sampling.startTime != null ? moment(sampling.startTime) : null;
                sampling.endTime = sampling.endTime != null ? moment(sampling.endTime) : null;
            });
        }
        return res;
    }

    listSensorsByTargetSystem(tsId: string): Observable<HttpResponse<ISensor[]>> {
        return this.http.get<ISensor[]>(`${this.resourceUrl}/sensors/${tsId}`, { observe: 'response' });
    }
}
