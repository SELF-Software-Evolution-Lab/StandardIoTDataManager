import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBatchTask } from 'app/shared/model/batch-task.model';

type EntityResponseType = HttpResponse<IBatchTask>;
type EntityArrayResponseType = HttpResponse<IBatchTask[]>;

@Injectable({ providedIn: 'root' })
export class BatchTaskService {
    public resourceUrl = SERVER_API_URL + 'api/batch-tasks';

    constructor(protected http: HttpClient) {}

    create(batchTask: IBatchTask): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(batchTask);
        return this.http
            .post<IBatchTask>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(batchTask: IBatchTask): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(batchTask);
        return this.http
            .put<IBatchTask>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IBatchTask>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBatchTask[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryMyUploads(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBatchTask[]>(`${this.resourceUrl}/upload-files`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryMyReports(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBatchTask[]>(`${this.resourceUrl}/search-reports`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    locateReport(id: string): string {
        return `${this.resourceUrl}/search-reports/file/${id}`;
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(batchTask: IBatchTask): IBatchTask {
        const copy: IBatchTask = Object.assign({}, batchTask, {
            startDate: batchTask.startDate != null && batchTask.startDate.isValid() ? batchTask.startDate.toJSON() : null,
            endDate: batchTask.endDate != null && batchTask.endDate.isValid() ? batchTask.endDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
            res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((batchTask: IBatchTask) => {
                batchTask.startDate = batchTask.startDate != null ? moment(batchTask.startDate) : null;
                batchTask.endDate = batchTask.endDate != null ? moment(batchTask.endDate) : null;
            });
        }
        return res;
    }
}
