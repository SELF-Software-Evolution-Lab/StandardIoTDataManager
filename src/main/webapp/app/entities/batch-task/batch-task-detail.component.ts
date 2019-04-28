import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBatchTask } from 'app/shared/model/batch-task.model';

@Component({
    selector: 'jhi-batch-task-detail',
    templateUrl: './batch-task-detail.component.html'
})
export class BatchTaskDetailComponent implements OnInit {
    batchTask: IBatchTask;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ batchTask }) => {
            this.batchTask = batchTask;
        });
    }

    previousState() {
        window.history.back();
    }
}
