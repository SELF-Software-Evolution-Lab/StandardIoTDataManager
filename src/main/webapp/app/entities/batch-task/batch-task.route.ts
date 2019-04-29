import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BatchTask } from 'app/shared/model/batch-task.model';
import { BatchTaskService } from './batch-task.service';
import { BatchTaskComponent } from './batch-task.component';
import { BatchTaskDetailComponent } from './batch-task-detail.component';
import { BatchTaskUpdateComponent } from './batch-task-update.component';
import { BatchTaskDeletePopupComponent } from './batch-task-delete-dialog.component';
import { IBatchTask } from 'app/shared/model/batch-task.model';

@Injectable({ providedIn: 'root' })
export class BatchTaskResolve implements Resolve<IBatchTask> {
    constructor(private service: BatchTaskService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBatchTask> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<BatchTask>) => response.ok),
                map((batchTask: HttpResponse<BatchTask>) => batchTask.body)
            );
        }
        return of(new BatchTask());
    }
}

export const batchTaskRoute: Routes = [
    {
        path: '',
        component: BatchTaskComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'BatchTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'search-report',
        component: BatchTaskComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'BatchTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: BatchTaskDetailComponent,
        resolve: {
            batchTask: BatchTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BatchTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'search-report/:id/view',
        component: BatchTaskDetailComponent,
        resolve: {
            batchTask: BatchTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BatchTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: BatchTaskUpdateComponent,
        resolve: {
            batchTask: BatchTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BatchTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: BatchTaskUpdateComponent,
        resolve: {
            batchTask: BatchTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BatchTasks'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const batchTaskPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: BatchTaskDeletePopupComponent,
        resolve: {
            batchTask: BatchTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BatchTasks'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
