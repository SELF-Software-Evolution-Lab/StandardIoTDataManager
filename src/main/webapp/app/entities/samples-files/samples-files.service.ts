import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { ISamplesFiles } from 'app/shared/model/samples-files.model';

type EntityResponseType = HttpResponse<ISamplesFiles>;

@Injectable({ providedIn: 'root' })
export class SamplesFilesService {
    public resourceUrl = SERVER_API_URL + 'api/samples-files';

    constructor(protected http: HttpClient) {}

    create2(file: File): Observable<EntityResponseType> {
        const formData: FormData = new FormData();
        formData.append('file', file);
        console.log('formulario:');
        console.log(formData);
        return this.http
            .post<ISamplesFiles>(this.resourceUrl + '-2', formData, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
        }
        return res;
    }
}
