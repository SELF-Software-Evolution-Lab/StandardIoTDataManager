import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Experiment, IExperiment } from 'app/shared/model/experiment.model';
import { ExperimentService } from './experiment.service';
import { ExperimentComponent } from './experiment.component';
import { ExperimentDetailComponent } from './experiment-detail.component';
import { ExperimentUpdateComponent } from './experiment-update.component';
import { ExperimentDeletePopupComponent } from './experiment-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class ExperimentResolve implements Resolve<IExperiment> {
    constructor(private service: ExperimentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IExperiment> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Experiment>) => response.ok),
                map((experiment: HttpResponse<Experiment>) => experiment.body)
            );
        }
        return of(new Experiment());
    }
}

export const experimentRoute: Routes = [
    {
        path: '',
        component: ExperimentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'name,asc',
            pageTitle: 'Experiments'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ExperimentDetailComponent,
        resolve: {
            experiment: ExperimentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiment Detail'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ExperimentUpdateComponent,
        resolve: {
            experiment: ExperimentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'New Experiment'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ExperimentUpdateComponent,
        resolve: {
            experiment: ExperimentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Edit Experiment'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const experimentPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ExperimentDeletePopupComponent,
        resolve: {
            experiment: ExperimentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Experiments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
