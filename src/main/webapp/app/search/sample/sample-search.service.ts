import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { ISampleSearchParameters } from 'app/shared/model/sample-search-parameters.model';

@Injectable({ providedIn: 'root' })
export class SearchSampleService {
    public resourceUrl = SERVER_API_URL + 'api/samples/data';

    constructor(protected http: HttpClient) {}

    search(searchParameters: ISampleSearchParameters): Observable<HttpResponse<Number>> {
        const copy = this.convertDateFromClient(searchParameters);
        console.log('to search', this.resourceUrl, copy);
        return this.http.post<Number>(this.resourceUrl, copy, { observe: 'response' }).pipe();
    }

    protected convertDateFromClient(searchParameters: ISampleSearchParameters): ISampleSearchParameters {
        const copy: ISampleSearchParameters = Object.assign({}, searchParameters, {
            fromDateTime:
                searchParameters.fromDateTime != null && searchParameters.fromDateTime.isValid()
                    ? searchParameters.fromDateTime.toJSON()
                    : null,
            toDateTime:
                searchParameters.toDateTime != null && searchParameters.toDateTime.isValid() ? searchParameters.toDateTime.toJSON() : null
        });
        return copy;
    }

    // protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    //     if (res.body) {
    //         res.body.ts = res.body.ts != null ? moment(res.body.ts) : null;
    //     }
    //     return res;
    // }
    //
    // protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    //     if (res.body) {
    //         res.body.forEach((sample: ISample) => {
    //             sample.ts = sample.ts != null ? moment(sample.ts) : null;
    //         });
    //     }
    //     return res;
    // }
}
