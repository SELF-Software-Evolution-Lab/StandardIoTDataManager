import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubSet } from 'app/shared/model/sub-set.model';

@Component({
    selector: 'jhi-sub-set-detail',
    templateUrl: './sub-set-detail.component.html'
})
export class SubSetDetailComponent implements OnInit {
    subSet: ISubSet;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ subSet }) => {
            this.subSet = subSet;
        });
    }

    previousState() {
        window.history.back();
    }
}
