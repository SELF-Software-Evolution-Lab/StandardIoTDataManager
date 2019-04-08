import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISamplesFiles } from 'app/shared/model/samples-files.model';
import { AccountService } from 'app/core';
import { SamplesFilesService } from './samples-files.service';

@Component({
    selector: 'jhi-samples-files',
    templateUrl: './samples-files.component.html'
})
export class SamplesFilesComponent implements OnInit, OnDestroy {
    samplesFiles: ISamplesFiles[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected samplesFilesService: SamplesFilesService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.samplesFilesService
            .query()
            .pipe(
                filter((res: HttpResponse<ISamplesFiles[]>) => res.ok),
                map((res: HttpResponse<ISamplesFiles[]>) => res.body)
            )
            .subscribe(
                (res: ISamplesFiles[]) => {
                    this.samplesFiles = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSamplesFiles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISamplesFiles) {
        return item.id;
    }

    registerChangeInSamplesFiles() {
        this.eventSubscriber = this.eventManager.subscribe('samplesFilesListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
