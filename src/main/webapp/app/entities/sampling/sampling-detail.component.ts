import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISampling } from 'app/shared/model/sampling.model';

@Component({
    selector: 'jhi-sampling-detail',
    templateUrl: './sampling-detail.component.html'
})
export class SamplingDetailComponent implements OnInit {
    sampling: ISampling;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sampling }) => {
            this.sampling = sampling;
        });
    }

    previousState() {
        window.history.back();
    }
}
