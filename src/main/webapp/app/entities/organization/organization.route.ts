import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';
import {UserRouteAccessService} from 'app/core';
import {Observable, of} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {IOrganization, Organization} from 'app/shared/model/organization.model';
import {OrganizationService} from './organization.service';
import {OrganizationComponent} from './organization.component';
import {OrganizationDetailComponent} from './organization-detail.component';
import {OrganizationUpdateComponent} from './organization-update.component';
import {OrganizationDeletePopupComponent} from './organization-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class OrganizationResolve implements Resolve<IOrganization> {
    constructor(private service: OrganizationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOrganization> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Organization>) => response.ok),
                map((organization: HttpResponse<Organization>) => organization.body)
            );
        }
        return of(new Organization());
    }
}

export const organizationRoute: Routes = [
    {
        path: '',
        component: OrganizationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Organizations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OrganizationDetailComponent,
        resolve: {
            organization: OrganizationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Organizations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OrganizationUpdateComponent,
        resolve: {
            organization: OrganizationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Organizations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OrganizationUpdateComponent,
        resolve: {
            organization: OrganizationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Organizations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const organizationPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OrganizationDeletePopupComponent,
        resolve: {
            organization: OrganizationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Organizations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
