import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { SamplesFilesService } from './samples-files.service';
import { ISamplesFiles } from 'app/shared/model/samples-files.model';
import { ISampling } from 'app/shared/model/sampling.model';
import { SamplingService } from '../sampling/sampling.service';
import { JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { ISample } from 'app/shared/model/sample.model';

@Component({
    selector: 'jhi-samples-files-update',
    templateUrl: './samples-files-update.component.html'
})
export class SamplesFilesUpdateComponent implements OnInit {
    isSaving: boolean;
    file: File;
    samplings: ISampling[];
    valueSampling: string = '';

    @Output() public visibleFileUpload: EventEmitter<string> = new EventEmitter<string>();
    constructor(
        protected samplesFilesService: SamplesFilesService,
        protected activatedRoute: ActivatedRoute,
        protected samplingService: SamplingService,
        protected jhiAlertService: JhiAlertService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.loadAll();
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;

        this.samplesFilesService.visibleFileUpload.emit(0);
        let _this = this;

        console.log('Si existe la variable', localStorage);
        console.log('Si existe la variable', sessionStorage);
        let token = this.getToken();

        var worker = new Worker('content/js/worker.js');
        worker.postMessage({ file: this.file, samplingId: this.valueSampling, token: token });
        worker.onmessage = function(e) {
            console.log('Resopuesta worker : ', e);
            if (e.data.indexOf('Error worker') == -1) {
                //No hay errores
                _this.samplesFilesService.visibleFileUpload.emit(1);
            } else {
                _this.samplesFilesService.visibleFileUpload.emit(-1);
            }

            _this.isSaving = false;
        };
        //this.subscribeToSaveResponse(this.samplesFilesService.create2(this.file, this.valueSampling));
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

    //AGREGAR SELECCION DE SAMPLING PARA LA CARGA DE ARCHIVO
    loadAll() {
        this.samplingService
            .query({
                page: 0,
                size: 1000,
                sort: ['name,asc']
            })
            .subscribe(
                (res: HttpResponse<ISampling[]>) => {
                    this.paginateSamplings(res.body, res.headers);
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            );
    }

    protected paginateSamplings(data: ISampling[], headers: HttpHeaders) {
        this.samplings = data;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackId(index: number, item: ISample) {
        return item.id;
    }

    getToken() {
        let token = '';
        if (sessionStorage) {
            if (sessionStorage.getItem('jhi-authenticationtoken')) {
                token = sessionStorage.getItem('jhi-authenticationtoken');
            }
        }
        return token;
    }
}
