import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILaboratory } from 'app/shared/model/laboratory.model';

type EntityResponseType = HttpResponse<ILaboratory>;
type EntityArrayResponseType = HttpResponse<ILaboratory[]>;
type StringArrayResponseType = HttpResponse<String[]>;

@Injectable({ providedIn: 'root' })
export class LaboratoryService {
    public resourceUrl = SERVER_API_URL + 'api/laboratories';

    constructor(protected http: HttpClient) {}

    create(laboratory: ILaboratory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(laboratory);
        return this.http
            .post<ILaboratory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(laboratory: ILaboratory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(laboratory);
        return this.http
            .put<ILaboratory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ILaboratory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ILaboratory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(laboratory: ILaboratory): ILaboratory {
        const copy: ILaboratory = Object.assign({}, laboratory, {
            dateCreated: laboratory.dateCreated != null && laboratory.dateCreated.isValid() ? laboratory.dateCreated.toJSON() : null,
            shareValidThru:
                laboratory.shareValidThru != null && laboratory.shareValidThru.isValid() ? laboratory.shareValidThru.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
            res.body.shareValidThru = res.body.shareValidThru != null ? moment(res.body.shareValidThru) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((laboratory: ILaboratory) => {
                laboratory.dateCreated = laboratory.dateCreated != null ? moment(laboratory.dateCreated) : null;
                laboratory.shareValidThru = laboratory.shareValidThru != null ? moment(laboratory.shareValidThru) : null;
            });
        }
        return res;
    }

    findSharedFiles(id: string): Observable<StringArrayResponseType> {
        return this.http.get<String[]>(`${this.resourceUrl}/files/${id}`, { observe: 'response' });
    }
}
