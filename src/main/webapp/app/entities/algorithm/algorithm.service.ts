import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAlgorithm } from 'app/shared/model/algorithm.model';

type EntityResponseType = HttpResponse<IAlgorithm>;
type EntityArrayResponseType = HttpResponse<IAlgorithm[]>;

@Injectable({ providedIn: 'root' })
export class AlgorithmService {
    public resourceUrl = SERVER_API_URL + 'api/algorithms';

    constructor(protected http: HttpClient) {}

    create(algorithm: IAlgorithm): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(algorithm);
        return this.http
            .post<IAlgorithm>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(algorithm: IAlgorithm): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(algorithm);
        return this.http
            .put<IAlgorithm>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IAlgorithm>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAlgorithm[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(algorithm: IAlgorithm): IAlgorithm {
        const copy: IAlgorithm = Object.assign({}, algorithm, {
            dateCreated: algorithm.dateCreated != null && algorithm.dateCreated.isValid() ? algorithm.dateCreated.toJSON() : null,
            lastSuccessfulRun:
                algorithm.lastSuccessfulRun != null && algorithm.lastSuccessfulRun.isValid() ? algorithm.lastSuccessfulRun.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
            res.body.lastSuccessfulRun = res.body.lastSuccessfulRun != null ? moment(res.body.lastSuccessfulRun) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((algorithm: IAlgorithm) => {
                algorithm.dateCreated = algorithm.dateCreated != null ? moment(algorithm.dateCreated) : null;
                algorithm.lastSuccessfulRun = algorithm.lastSuccessfulRun != null ? moment(algorithm.lastSuccessfulRun) : null;
            });
        }
        return res;
    }
}
