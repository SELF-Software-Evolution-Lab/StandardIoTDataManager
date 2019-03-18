import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ITargetSystem, TargetSystem } from 'app/shared/model/target-system.model';
import { TargetSystemService } from './target-system.service';
import { TargetSystemComponent } from './target-system.component';
import { TargetSystemDetailComponent } from './target-system-detail.component';
import { TargetSystemUpdateComponent } from './target-system-update.component';
import { TargetSystemDeletePopupComponent } from './target-system-delete-dialog.component';
import { OperativeRangeUpdateComponent } from 'app/entities/target-system/operative-range-update.component';

@Injectable({ providedIn: 'root' })
export class TargetSystemResolve implements Resolve<ITargetSystem> {
    constructor(private service: TargetSystemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITargetSystem> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<TargetSystem>) => response.ok),
                map((targetSystem: HttpResponse<TargetSystem>) => targetSystem.body)
            );
        }
        return of(new TargetSystem());
    }
}

export const targetSystemRoute: Routes = [
    {
        path: '',
        component: TargetSystemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'TargetSystems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: TargetSystemDetailComponent,
        resolve: {
            targetSystem: TargetSystemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TargetSystems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new/operative-range',
        component: OperativeRangeUpdateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TargetSystems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: TargetSystemUpdateComponent,
        resolve: {
            targetSystem: TargetSystemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TargetSystems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/operative-range',
        component: OperativeRangeUpdateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TargetSystems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: TargetSystemUpdateComponent,
        resolve: {
            targetSystem: TargetSystemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TargetSystems'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const targetSystemPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: TargetSystemDeletePopupComponent,
        resolve: {
            targetSystem: TargetSystemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TargetSystems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
