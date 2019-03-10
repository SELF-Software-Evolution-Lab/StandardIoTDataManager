import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
        return this.http.post<IExperiment>(this.resourceUrl, experiment, { observe: 'response' });
    }

    update(experiment: IExperiment): Observable<EntityResponseType> {
        return this.http.put<IExperiment>(this.resourceUrl, experiment, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IExperiment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExperiment[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
