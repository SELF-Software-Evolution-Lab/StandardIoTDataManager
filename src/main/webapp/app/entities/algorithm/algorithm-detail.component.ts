import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IAlgorithm } from 'app/shared/model/algorithm.model';

@Component({
    selector: 'jhi-algorithm-detail',
    templateUrl: './algorithm-detail.component.html'
})
export class AlgorithmDetailComponent implements OnInit {
    algorithm: IAlgorithm;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ algorithm }) => {
            this.algorithm = algorithm;
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
