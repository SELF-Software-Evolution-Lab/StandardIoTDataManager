import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISample } from 'app/shared/model/sample.model';

@Component({
    selector: 'jhi-sample-detail',
    templateUrl: './sample-detail.component.html'
})
export class SampleDetailComponent implements OnInit {
    sample: ISample;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sample }) => {
            this.sample = sample;
        });
    }

    previousState() {
        window.history.back();
    }
}
