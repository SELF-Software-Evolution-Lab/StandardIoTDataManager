import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IExperiment } from 'app/shared/model/experiment.model';

@Component({
    selector: 'jhi-experiment-detail',
    templateUrl: './experiment-detail.component.html'
})
export class ExperimentDetailComponent implements OnInit {
    experiment: IExperiment;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ experiment }) => {
            this.experiment = experiment;
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
