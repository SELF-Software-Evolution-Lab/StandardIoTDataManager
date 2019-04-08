import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SamplesFiles } from 'app/shared/model/samples-files.model';
import { SamplesFilesService } from './samples-files.service';
import { SamplesFilesComponent } from './samples-files.component';
import { SamplesFilesDetailComponent } from './samples-files-detail.component';
import { SamplesFilesUpdateComponent } from './samples-files-update.component';
import { SamplesFilesDeletePopupComponent } from './samples-files-delete-dialog.component';
import { ISamplesFiles } from 'app/shared/model/samples-files.model';

@Injectable({ providedIn: 'root' })
export class SamplesFilesResolve implements Resolve<ISamplesFiles> {
    constructor(private service: SamplesFilesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISamplesFiles> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SamplesFiles>) => response.ok),
                map((samplesFiles: HttpResponse<SamplesFiles>) => samplesFiles.body)
            );
        }
        return of(new SamplesFiles());
    }
}

export const samplesFilesRoute: Routes = [
    {
        path: '',
        component: SamplesFilesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SamplesFiles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SamplesFilesDetailComponent,
        resolve: {
            samplesFiles: SamplesFilesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SamplesFiles'
        },
        canActivate: [UserRouteAccessService]
    },
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
    },
    {
        path: ':id/edit',
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

export const samplesFilesPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SamplesFilesDeletePopupComponent,
        resolve: {
            samplesFiles: SamplesFilesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SamplesFiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
