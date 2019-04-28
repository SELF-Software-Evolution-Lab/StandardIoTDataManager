import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SamplesFilesService } from './samples-files.service';
import { SamplesFilesUpdateComponent } from './samples-files-update.component';
import { ISamplesFiles, SamplesFiles } from 'app/shared/model/samples-files.model';

@Injectable({ providedIn: 'root' })
export class SamplesFilesResolve implements Resolve<ISamplesFiles> {
    constructor(private service: SamplesFilesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISamplesFiles> {
        const id = route.params['id'] ? route.params['id'] : null;
        return of(new SamplesFiles());
    }
}

export const samplesFilesRoute: Routes = [
    {
        path: 'new',
        component: SamplesFilesUpdateComponent,
        resolve: {
            samplesFiles: SamplesFilesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SamplesFiles'
        },
        canActivate: [UserRouteAccessService]
    }
];
