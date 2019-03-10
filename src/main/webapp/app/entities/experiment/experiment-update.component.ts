import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IExperiment } from 'app/shared/model/experiment.model';
import { ExperimentService } from './experiment.service';
import { ITargetSystem } from 'app/shared/model/target-system.model';
import { TargetSystemService } from 'app/entities/target-system';

@Component({
    selector: 'jhi-experiment-update',
    templateUrl: './experiment-update.component.html'
})
export class ExperimentUpdateComponent implements OnInit {
    experiment: IExperiment;
    isSaving: boolean;

    systems: ITargetSystem[];
    created: string;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected experimentService: ExperimentService,
        protected targetSystemService: TargetSystemService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ experiment }) => {
            this.experiment = experiment;
            this.created = this.experiment.created != null ? this.experiment.created.format(DATE_TIME_FORMAT) : null;
        });
        this.targetSystemService
            .query({ filter: 'experiment-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<ITargetSystem[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITargetSystem[]>) => response.body)
            )
            .subscribe(
                (res: ITargetSystem[]) => {
                    if (!this.experiment.systemId) {
                        this.systems = res;
                    } else {
                        this.targetSystemService
                            .find(this.experiment.systemId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<ITargetSystem>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<ITargetSystem>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: ITargetSystem) => (this.systems = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.experiment.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        if (this.experiment.id !== undefined) {
            this.subscribeToSaveResponse(this.experimentService.update(this.experiment));
        } else {
            this.subscribeToSaveResponse(this.experimentService.create(this.experiment));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IExperiment>>) {
        result.subscribe((res: HttpResponse<IExperiment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackTargetSystemById(index: number, item: ITargetSystem) {
        return item.id;
    }
}
