import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITargetSystem } from 'app/shared/model/target-system.model';

@Component({
    selector: 'jhi-target-system-detail',
    templateUrl: './target-system-detail.component.html'
})
export class TargetSystemDetailComponent implements OnInit {
    targetSystem: ITargetSystem;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ targetSystem }) => {
            this.targetSystem = targetSystem;
        });
    }

    previousState() {
        window.history.back();
    }
}
