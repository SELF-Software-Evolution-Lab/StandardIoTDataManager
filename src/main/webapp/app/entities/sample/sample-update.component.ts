import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ISample } from 'app/shared/model/sample.model';
import { SampleService } from './sample.service';

@Component({
    selector: 'jhi-sample-update',
    templateUrl: './sample-update.component.html'
})
export class SampleUpdateComponent implements OnInit {
    sample: ISample;
    isSaving: boolean;
    ts: string;

    constructor(protected sampleService: SampleService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sample }) => {
            this.sample = sample;
            this.ts = this.sample.ts != null ? this.sample.ts.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.sample.ts = this.ts != null ? moment(this.ts, DATE_TIME_FORMAT) : null;
        if (this.sample.id !== undefined) {
            this.subscribeToSaveResponse(this.sampleService.update(this.sample));
        } else {
            this.subscribeToSaveResponse(this.sampleService.create(this.sample));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISample>>) {
        result.subscribe((res: HttpResponse<ISample>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
