import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISamplesFiles } from 'app/shared/model/samples-files.model';

type EntityResponseType = HttpResponse<ISamplesFiles>;
type EntityArrayResponseType = HttpResponse<ISamplesFiles[]>;

@Injectable({ providedIn: 'root' })
export class SamplesFilesService {
    public resourceUrl = SERVER_API_URL + 'api/samples-files';

    constructor(protected http: HttpClient) {}

    create(samplesFiles: ISamplesFiles): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(samplesFiles);
        return this.http
            .post<ISamplesFiles>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    create2(file: File): Observable<EntityResponseType> {
        const formData: FormData = new FormData();
        formData.append('file', file);
        console.log('formulario:');
        console.log(formData);
        return this.http
            .post<ISamplesFiles>(this.resourceUrl + '-2', formData, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(samplesFiles: ISamplesFiles): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(samplesFiles);
        return this.http
            .put<ISamplesFiles>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ISamplesFiles>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISamplesFiles[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(samplesFiles: ISamplesFiles): ISamplesFiles {
        const copy: ISamplesFiles = Object.assign({}, samplesFiles, {
            createDateTime:
                samplesFiles.createDateTime != null && samplesFiles.createDateTime.isValid()
                    ? samplesFiles.createDateTime.format(DATE_FORMAT)
                    : null,
            updateDateTime:
                samplesFiles.updateDateTime != null && samplesFiles.updateDateTime.isValid()
                    ? samplesFiles.updateDateTime.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createDateTime = res.body.createDateTime != null ? moment(res.body.createDateTime) : null;
            res.body.updateDateTime = res.body.updateDateTime != null ? moment(res.body.updateDateTime) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((samplesFiles: ISamplesFiles) => {
                samplesFiles.createDateTime = samplesFiles.createDateTime != null ? moment(samplesFiles.createDateTime) : null;
                samplesFiles.updateDateTime = samplesFiles.updateDateTime != null ? moment(samplesFiles.updateDateTime) : null;
            });
        }
        return res;
    }
}
