import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiDataUtils } from 'ng-jhipster';
import { ISampleFiles } from 'app/shared/model/sample-files.model';
import { SampleFilesService } from './sample-files.service';

@Component({
    selector: 'jhi-sample-files-update',
    templateUrl: './sample-files-update.component.html'
})
export class SampleFilesUpdateComponent implements OnInit {
    sampleFiles: ISampleFiles;
    isSaving: boolean;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected sampleFilesService: SampleFilesService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sampleFiles }) => {
            this.sampleFiles = sampleFiles;
        });
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
        if (this.sampleFiles.id !== undefined) {
            this.subscribeToSaveResponse(this.sampleFilesService.update(this.sampleFiles));
        } else {
            this.subscribeToSaveResponse(this.sampleFilesService.create(this.sampleFiles));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISampleFiles>>) {
        result.subscribe((res: HttpResponse<ISampleFiles>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
