import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISampleFiles } from 'app/shared/model/sample-files.model';

type EntityResponseType = HttpResponse<ISampleFiles>;
type EntityArrayResponseType = HttpResponse<ISampleFiles[]>;

@Injectable({ providedIn: 'root' })
export class SampleFilesService {
    public resourceUrl = SERVER_API_URL + 'api/sample-files';

    constructor(protected http: HttpClient) {}

    create(sampleFiles: ISampleFiles): Observable<EntityResponseType> {
        return this.http.post<ISampleFiles>(this.resourceUrl, sampleFiles, { observe: 'response' });
    }

    update(sampleFiles: ISampleFiles): Observable<EntityResponseType> {
        return this.http.put<ISampleFiles>(this.resourceUrl, sampleFiles, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ISampleFiles>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISampleFiles[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
