import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from './algorithm.service';
import { ILaboratory } from 'app/shared/model/laboratory.model';
import { LaboratoryService } from 'app/entities/laboratory';

@Component({
    selector: 'jhi-algorithm-update',
    templateUrl: './algorithm-update.component.html'
})
export class AlgorithmUpdateComponent implements OnInit {
    algorithm: IAlgorithm;
    isSaving: boolean;

    laboratories: ILaboratory[];
    dateCreated: string;
    lastSuccessfulRun: string;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected algorithmService: AlgorithmService,
        protected laboratoryService: LaboratoryService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ algorithm }) => {
            this.algorithm = algorithm;
            this.dateCreated = this.algorithm.dateCreated != null ? this.algorithm.dateCreated.format(DATE_TIME_FORMAT) : null;
            this.lastSuccessfulRun =
                this.algorithm.lastSuccessfulRun != null ? this.algorithm.lastSuccessfulRun.format(DATE_TIME_FORMAT) : null;
        });
        this.laboratoryService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ILaboratory[]>) => mayBeOk.ok),
                map((response: HttpResponse<ILaboratory[]>) => response.body)
            )
            .subscribe((res: ILaboratory[]) => (this.laboratories = res), (res: HttpErrorResponse) => this.onError(res.message));
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
        this.algorithm.dateCreated = this.dateCreated != null ? moment(this.dateCreated, DATE_TIME_FORMAT) : moment();
        this.algorithm.lastSuccessfulRun = this.lastSuccessfulRun != null ? moment(this.lastSuccessfulRun, DATE_TIME_FORMAT) : null;
        if (this.algorithm.id !== undefined) {
            this.subscribeToSaveResponse(this.algorithmService.update(this.algorithm));
        } else {
            this.subscribeToSaveResponse(this.algorithmService.create(this.algorithm));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlgorithm>>) {
        result.subscribe((res: HttpResponse<IAlgorithm>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackLaboratoryById(index: number, item: ILaboratory) {
        return item.id;
    }
}
