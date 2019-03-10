import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ISensor, Sensor } from 'app/shared/model/sensor.model';
import { SensorService } from './sensor.service';
import { SensorComponent } from './sensor.component';
import { SensorDetailComponent } from './sensor-detail.component';
import { SensorUpdateComponent } from './sensor-update.component';
import { SensorDeletePopupComponent } from './sensor-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class SensorResolve implements Resolve<ISensor> {
    constructor(private service: SensorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISensor> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Sensor>) => response.ok),
                map((sensor: HttpResponse<Sensor>) => sensor.body)
            );
        }
        return of(new Sensor());
    }
}

export const sensorRoute: Routes = [
    {
        path: '',
        component: SensorComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Sensors'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SensorDetailComponent,
        resolve: {
            sensor: SensorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sensors'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SensorUpdateComponent,
        resolve: {
            sensor: SensorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sensors'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SensorUpdateComponent,
        resolve: {
            sensor: SensorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sensors'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sensorPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SensorDeletePopupComponent,
        resolve: {
            sensor: SensorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sensors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
