import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SampleFiles } from 'app/shared/model/sample-files.model';
import { SampleFilesService } from './sample-files.service';
import { SampleFilesComponent } from './sample-files.component';
import { SampleFilesDetailComponent } from './sample-files-detail.component';
import { SampleFilesUpdateComponent } from './sample-files-update.component';
import { SampleFilesDeletePopupComponent } from './sample-files-delete-dialog.component';
import { ISampleFiles } from 'app/shared/model/sample-files.model';

@Injectable({ providedIn: 'root' })
export class SampleFilesResolve implements Resolve<ISampleFiles> {
    constructor(private service: SampleFilesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISampleFiles> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SampleFiles>) => response.ok),
                map((sampleFiles: HttpResponse<SampleFiles>) => sampleFiles.body)
            );
        }
        return of(new SampleFiles());
    }
}

export const sampleFilesRoute: Routes = [
    {
        path: '',
        component: SampleFilesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SampleFiles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SampleFilesDetailComponent,
        resolve: {
            sampleFiles: SampleFilesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SampleFiles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SampleFilesUpdateComponent,
        resolve: {
            sampleFiles: SampleFilesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SampleFiles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SampleFilesUpdateComponent,
        resolve: {
            sampleFiles: SampleFilesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SampleFiles'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sampleFilesPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SampleFilesDeletePopupComponent,
        resolve: {
            sampleFiles: SampleFilesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SampleFiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
