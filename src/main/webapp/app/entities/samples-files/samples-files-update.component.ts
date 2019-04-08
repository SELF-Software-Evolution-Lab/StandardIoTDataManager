import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { ISamplesFiles } from 'app/shared/model/samples-files.model';
import { SamplesFilesService } from './samples-files.service';

@Component({
    selector: 'jhi-samples-files-update',
    templateUrl: './samples-files-update.component.html'
})
export class SamplesFilesUpdateComponent implements OnInit {
    samplesFiles: ISamplesFiles;
    isSaving: boolean;
    createDateTimeDp: any;
    updateDateTimeDp: any;

    constructor(protected samplesFilesService: SamplesFilesService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ samplesFiles }) => {
            this.samplesFiles = samplesFiles;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.samplesFiles.id !== undefined) {
            this.subscribeToSaveResponse(this.samplesFilesService.update(this.samplesFiles));
        } else {
            this.subscribeToSaveResponse(this.samplesFilesService.create(this.samplesFiles));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISamplesFiles>>) {
        result.subscribe((res: HttpResponse<ISamplesFiles>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
