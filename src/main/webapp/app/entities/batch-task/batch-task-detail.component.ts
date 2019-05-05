import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBatchTask, TaskState, TaskType } from 'app/shared/model/batch-task.model';
import { BatchTaskService } from 'app/entities/batch-task/batch-task.service';

@Component({
    selector: 'jhi-batch-task-detail',
    templateUrl: './batch-task-detail.component.html'
})
export class BatchTaskDetailComponent implements OnInit {
    taskTypeFileLoad = TaskType.FILELOAD;
    taskTypeReport = TaskType.REPORT;

    batchTask: IBatchTask;

    constructor(protected activatedRoute: ActivatedRoute, protected batchTaskService: BatchTaskService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ batchTask }) => {
            this.batchTask = batchTask;
        });
    }

    previousState() {
        window.history.back();
    }

    locateReport(id: string): string {
        return this.batchTaskService.locateReport(id);
    }

    isCompleted(state: TaskState) {
        return state === TaskState.COMPLETED;
    }

    isError(state: TaskState) {
        return state === TaskState.ERROR;
    }
}
