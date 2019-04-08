import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISamplesFiles } from 'app/shared/model/samples-files.model';

@Component({
    selector: 'jhi-samples-files-detail',
    templateUrl: './samples-files-detail.component.html'
})
export class SamplesFilesDetailComponent implements OnInit {
    samplesFiles: ISamplesFiles;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ samplesFiles }) => {
            this.samplesFiles = samplesFiles;
        });
    }

    previousState() {
        window.history.back();
    }
}
