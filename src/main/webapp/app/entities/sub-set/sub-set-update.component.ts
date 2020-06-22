import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ISubSet } from 'app/shared/model/sub-set.model';
import { SubSetService } from './sub-set.service';
import { ILaboratory } from 'app/shared/model/laboratory.model';
import { LaboratoryService } from 'app/entities/laboratory';
import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from 'app/entities/algorithm';

@Component({
    selector: 'jhi-sub-set-update',
    templateUrl: './sub-set-update.component.html'
})
export class SubSetUpdateComponent implements OnInit {
    subSet: ISubSet;
    isSaving: boolean;

    laboratories: ILaboratory[];

    algorithms: IAlgorithm[];
    dateCreated: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected subSetService: SubSetService,
        protected laboratoryService: LaboratoryService,
        protected algorithmService: AlgorithmService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ subSet }) => {
            this.subSet = subSet;
            this.dateCreated = this.subSet.dateCreated != null ? this.subSet.dateCreated.format(DATE_TIME_FORMAT) : null;
        });
        this.laboratoryService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ILaboratory[]>) => mayBeOk.ok),
                map((response: HttpResponse<ILaboratory[]>) => response.body)
            )
            .subscribe((res: ILaboratory[]) => (this.laboratories = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.algorithmService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAlgorithm[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAlgorithm[]>) => response.body)
            )
            .subscribe((res: IAlgorithm[]) => (this.algorithms = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.subSet.dateCreated = this.dateCreated != null ? moment(this.dateCreated, DATE_TIME_FORMAT) : null;
        if (this.subSet.id !== undefined) {
            this.subscribeToSaveResponse(this.subSetService.update(this.subSet));
        } else {
            this.subscribeToSaveResponse(this.subSetService.create(this.subSet));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubSet>>) {
        result.subscribe((res: HttpResponse<ISubSet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAlgorithmById(index: number, item: IAlgorithm) {
        return item.id;
    }
}
