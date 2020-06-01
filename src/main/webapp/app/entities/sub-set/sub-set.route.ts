import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SubSet } from 'app/shared/model/sub-set.model';
import { SubSetService } from './sub-set.service';
import { SubSetComponent } from './sub-set.component';
import { SubSetDetailComponent } from './sub-set-detail.component';
import { SubSetUpdateComponent } from './sub-set-update.component';
import { SubSetDeletePopupComponent } from './sub-set-delete-dialog.component';
import { ISubSet } from 'app/shared/model/sub-set.model';

@Injectable({ providedIn: 'root' })
export class SubSetResolve implements Resolve<ISubSet> {
    constructor(private service: SubSetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISubSet> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SubSet>) => response.ok),
                map((subSet: HttpResponse<SubSet>) => subSet.body)
            );
        }
        return of(new SubSet());
    }
}

export const subSetRoute: Routes = [
    {
        path: '',
        component: SubSetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'SubSets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SubSetDetailComponent,
        resolve: {
            subSet: SubSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SubSets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SubSetUpdateComponent,
        resolve: {
            subSet: SubSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SubSets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SubSetUpdateComponent,
        resolve: {
            subSet: SubSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SubSets'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const subSetPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SubSetDeletePopupComponent,
        resolve: {
            subSet: SubSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SubSets'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
