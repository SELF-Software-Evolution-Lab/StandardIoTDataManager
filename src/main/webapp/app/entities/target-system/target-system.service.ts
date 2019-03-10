import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITargetSystem } from 'app/shared/model/target-system.model';

type EntityResponseType = HttpResponse<ITargetSystem>;
type EntityArrayResponseType = HttpResponse<ITargetSystem[]>;

@Injectable({ providedIn: 'root' })
export class TargetSystemService {
    public resourceUrl = SERVER_API_URL + 'api/target-systems';

    constructor(protected http: HttpClient) {}

    create(targetSystem: ITargetSystem): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(targetSystem);
        return this.http
            .post<ITargetSystem>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(targetSystem: ITargetSystem): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(targetSystem);
        return this.http
            .put<ITargetSystem>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ITargetSystem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITargetSystem[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(targetSystem: ITargetSystem): ITargetSystem {
        const copy: ITargetSystem = Object.assign({}, targetSystem, {
            created: targetSystem.created != null && targetSystem.created.isValid() ? targetSystem.created.toJSON() : null
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
            res.body.forEach((targetSystem: ITargetSystem) => {
                targetSystem.created = targetSystem.created != null ? moment(targetSystem.created) : null;
            });
        }
        return res;
    }
}
