import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { SamplesFilesService } from './samples-files.service';
import { ISamplesFiles } from 'app/shared/model/samples-files.model';

@Component({
    selector: 'jhi-samples-files-update',
    templateUrl: './samples-files-update.component.html'
})
export class SamplesFilesUpdateComponent implements OnInit {
    isSaving: boolean;
    file: File;

    constructor(protected samplesFilesService: SamplesFilesService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.subscribeToSaveResponse(this.samplesFilesService.create2(this.file));
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

    handleFileInput(files: FileList) {
        // console.log(files);
        if (files && files[0]) {
            // console.log('cargo archivo');
            // console.log(files[0]);
            this.file = files[0];
        }
    }
}
