import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ISampling, Sampling } from 'app/shared/model/sampling.model';
import { SamplingService } from './sampling.service';
import { SamplingComponent } from './sampling.component';
import { SamplingDetailComponent } from './sampling-detail.component';
import { SamplingUpdateComponent } from './sampling-update.component';
import { SamplingDeletePopupComponent } from './sampling-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class SamplingResolve implements Resolve<ISampling> {
    constructor(private service: SamplingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISampling> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Sampling>) => response.ok),
                map((sampling: HttpResponse<Sampling>) => sampling.body)
            );
        }
        return of(new Sampling());
    }
}

export const samplingRoute: Routes = [
    {
        path: '',
        component: SamplingComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Samplings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SamplingDetailComponent,
        resolve: {
            sampling: SamplingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Samplings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SamplingUpdateComponent,
        resolve: {
            sampling: SamplingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Samplings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SamplingUpdateComponent,
        resolve: {
            sampling: SamplingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Samplings'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const samplingPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SamplingDeletePopupComponent,
        resolve: {
            sampling: SamplingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Samplings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
