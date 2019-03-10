import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISampling } from 'app/shared/model/sampling.model';

@Component({
    selector: 'jhi-sampling-detail',
    templateUrl: './sampling-detail.component.html'
})
export class SamplingDetailComponent implements OnInit {
    sampling: ISampling;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sampling }) => {
            this.sampling = sampling;
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
