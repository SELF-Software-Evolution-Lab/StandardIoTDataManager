import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Algorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from './algorithm.service';
import { AlgorithmComponent } from './algorithm.component';
import { AlgorithmDetailComponent } from './algorithm-detail.component';
import { AlgorithmUpdateComponent } from './algorithm-update.component';
import { AlgorithmDeletePopupComponent } from './algorithm-delete-dialog.component';
import { IAlgorithm } from 'app/shared/model/algorithm.model';

@Injectable({ providedIn: 'root' })
export class AlgorithmResolve implements Resolve<IAlgorithm> {
    constructor(private service: AlgorithmService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAlgorithm> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Algorithm>) => response.ok),
                map((algorithm: HttpResponse<Algorithm>) => algorithm.body)
            );
        }
        return of(new Algorithm());
    }
}

export const algorithmRoute: Routes = [
    {
        path: '',
        component: AlgorithmComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AlgorithmDetailComponent,
        resolve: {
            algorithm: AlgorithmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AlgorithmUpdateComponent,
        resolve: {
            algorithm: AlgorithmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AlgorithmUpdateComponent,
        resolve: {
            algorithm: AlgorithmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const algorithmPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AlgorithmDeletePopupComponent,
        resolve: {
            algorithm: AlgorithmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Algorithms'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
