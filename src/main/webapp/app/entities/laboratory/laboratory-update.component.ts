import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ILaboratory } from 'app/shared/model/laboratory.model';
import { LaboratoryService } from './laboratory.service';
import { ISampling } from 'app/shared/model/sampling.model';
import { SamplingService } from 'app/entities/sampling';

@Component({
    selector: 'jhi-laboratory-update',
    templateUrl: './laboratory-update.component.html'
})
export class LaboratoryUpdateComponent implements OnInit {
    laboratory: ILaboratory;
    isSaving: boolean;

    samplings: ISampling[];
    dateCreated: string;
    shareValidThru: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected laboratoryService: LaboratoryService,
        protected samplingService: SamplingService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ laboratory }) => {
            this.laboratory = laboratory;
            this.dateCreated = this.laboratory.dateCreated != null ? this.laboratory.dateCreated.format(DATE_TIME_FORMAT) : null;
            this.shareValidThru = this.laboratory.shareValidThru != null ? this.laboratory.shareValidThru.format(DATE_TIME_FORMAT) : null;
        });
        this.samplingService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ISampling[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISampling[]>) => response.body)
            )
            .subscribe((res: ISampling[]) => (this.samplings = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.laboratory.dateCreated = this.dateCreated != null ? moment(this.dateCreated, DATE_TIME_FORMAT) : null;
        this.laboratory.shareValidThru = this.shareValidThru != null ? moment(this.shareValidThru, DATE_TIME_FORMAT) : null;
        if (this.laboratory.id !== undefined) {
            this.subscribeToSaveResponse(this.laboratoryService.update(this.laboratory));
        } else {
            this.subscribeToSaveResponse(this.laboratoryService.create(this.laboratory));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ILaboratory>>) {
        result.subscribe((res: HttpResponse<ILaboratory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSamplingById(index: number, item: ISampling) {
        return item.id;
    }
}
