import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Laboratory } from 'app/shared/model/laboratory.model';
import { LaboratoryService } from './laboratory.service';
import { LaboratoryComponent } from './laboratory.component';
import { LaboratoryDetailComponent } from './laboratory-detail.component';
import { LaboratoryUpdateComponent } from './laboratory-update.component';
import { LaboratoryDeletePopupComponent } from './laboratory-delete-dialog.component';
import { ILaboratory } from 'app/shared/model/laboratory.model';

@Injectable({ providedIn: 'root' })
export class LaboratoryResolve implements Resolve<ILaboratory> {
    constructor(private service: LaboratoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILaboratory> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Laboratory>) => response.ok),
                map((laboratory: HttpResponse<Laboratory>) => laboratory.body)
            );
        }
        return of(new Laboratory());
    }
}

export const laboratoryRoute: Routes = [
    {
        path: '',
        component: LaboratoryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Laboratories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: LaboratoryDetailComponent,
        resolve: {
            laboratory: LaboratoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Laboratories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: LaboratoryUpdateComponent,
        resolve: {
            laboratory: LaboratoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Laboratories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: LaboratoryUpdateComponent,
        resolve: {
            laboratory: LaboratoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Laboratories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const laboratoryPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: LaboratoryDeletePopupComponent,
        resolve: {
            laboratory: LaboratoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Laboratories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
