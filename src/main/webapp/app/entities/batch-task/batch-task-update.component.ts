import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IBatchTask } from 'app/shared/model/batch-task.model';
import { BatchTaskService } from './batch-task.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-batch-task-update',
    templateUrl: './batch-task-update.component.html'
})
export class BatchTaskUpdateComponent implements OnInit {
    batchTask: IBatchTask;
    isSaving: boolean;

    users: IUser[];
    startDate: string;
    endDate: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected batchTaskService: BatchTaskService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ batchTask }) => {
            this.batchTask = batchTask;
            this.startDate = this.batchTask.startDate != null ? this.batchTask.startDate.format(DATE_TIME_FORMAT) : null;
            this.endDate = this.batchTask.endDate != null ? this.batchTask.endDate.format(DATE_TIME_FORMAT) : null;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.batchTask.startDate = this.startDate != null ? moment(this.startDate, DATE_TIME_FORMAT) : null;
        this.batchTask.endDate = this.endDate != null ? moment(this.endDate, DATE_TIME_FORMAT) : null;
        if (this.batchTask.id !== undefined) {
            this.subscribeToSaveResponse(this.batchTaskService.update(this.batchTask));
        } else {
            this.subscribeToSaveResponse(this.batchTaskService.create(this.batchTask));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IBatchTask>>) {
        result.subscribe((res: HttpResponse<IBatchTask>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
