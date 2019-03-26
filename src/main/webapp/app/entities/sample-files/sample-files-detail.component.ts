import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISampleFiles } from 'app/shared/model/sample-files.model';

@Component({
    selector: 'jhi-sample-files-detail',
    templateUrl: './sample-files-detail.component.html'
})
export class SampleFilesDetailComponent implements OnInit {
    sampleFiles: ISampleFiles;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
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
    previousState() {
        window.history.back();
    }
}
