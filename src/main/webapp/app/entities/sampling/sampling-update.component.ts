import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ISampling } from 'app/shared/model/sampling.model';
import { SamplingService } from './sampling.service';
import { IExperiment } from 'app/shared/model/experiment.model';
import { ExperimentService } from 'app/entities/experiment';

@Component({
    selector: 'jhi-sampling-update',
    templateUrl: './sampling-update.component.html'
})
export class SamplingUpdateComponent implements OnInit {
    sampling: ISampling;
    isSaving: boolean;

    experiments: IExperiment[];
    startTime: string;
    endTime: string;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected samplingService: SamplingService,
        protected experimentService: ExperimentService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sampling }) => {
            this.sampling = sampling;
            this.startTime = this.sampling.startTime != null ? this.sampling.startTime.format(DATE_TIME_FORMAT) : null;
            this.endTime = this.sampling.endTime != null ? this.sampling.endTime.format(DATE_TIME_FORMAT) : null;
        });
        this.experimentService
            .query({ filter: 'sampling-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IExperiment[]>) => mayBeOk.ok),
                map((response: HttpResponse<IExperiment[]>) => response.body)
            )
            .subscribe(
                (res: IExperiment[]) => {
                    if (!this.sampling.experimentId) {
                        this.experiments = res;
                    } else {
                        this.experimentService
                            .find(this.sampling.experimentId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IExperiment>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IExperiment>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IExperiment) => (this.experiments = [subRes].concat(res)),
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
        this.sampling.startTime = this.startTime != null ? moment(this.startTime, DATE_TIME_FORMAT) : null;
        this.sampling.endTime = this.endTime != null ? moment(this.endTime, DATE_TIME_FORMAT) : null;
        if (this.sampling.id !== undefined) {
            this.subscribeToSaveResponse(this.samplingService.update(this.sampling));
        } else {
            this.subscribeToSaveResponse(this.samplingService.create(this.sampling));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISampling>>) {
        result.subscribe((res: HttpResponse<ISampling>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackExperimentById(index: number, item: IExperiment) {
        return item.id;
    }
}
