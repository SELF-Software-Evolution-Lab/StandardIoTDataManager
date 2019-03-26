import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ISampleFiles } from 'app/shared/model/sample-files.model';
import { AccountService } from 'app/core';
import { SampleFilesService } from './sample-files.service';

@Component({
    selector: 'jhi-sample-files',
    templateUrl: './sample-files.component.html'
})
export class SampleFilesComponent implements OnInit, OnDestroy {
    sampleFiles: ISampleFiles[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected sampleFilesService: SampleFilesService,
        protected jhiAlertService: JhiAlertService,
        protected dataUtils: JhiDataUtils,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.sampleFilesService
            .query()
            .pipe(
                filter((res: HttpResponse<ISampleFiles[]>) => res.ok),
                map((res: HttpResponse<ISampleFiles[]>) => res.body)
            )
            .subscribe(
                (res: ISampleFiles[]) => {
                    this.sampleFiles = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSampleFiles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISampleFiles) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInSampleFiles() {
        this.eventSubscriber = this.eventManager.subscribe('sampleFilesListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
