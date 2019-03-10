import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IExperiment } from 'app/shared/model/experiment.model';

type EntityResponseType = HttpResponse<IExperiment>;
type EntityArrayResponseType = HttpResponse<IExperiment[]>;

@Injectable({ providedIn: 'root' })
export class ExperimentService {
    public resourceUrl = SERVER_API_URL + 'api/experiments';

    constructor(protected http: HttpClient) {}

    create(experiment: IExperiment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(experiment);
        return this.http
            .post<IExperiment>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(experiment: IExperiment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(experiment);
        return this.http
            .put<IExperiment>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IExperiment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IExperiment[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(experiment: IExperiment): IExperiment {
        const copy: IExperiment = Object.assign({}, experiment, {
            created: experiment.created != null && experiment.created.isValid() ? experiment.created.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.created = res.body.created != null ? moment(res.body.created) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((experiment: IExperiment) => {
                experiment.created = experiment.created != null ? moment(experiment.created) : null;
            });
        }
        return res;
    }
}
