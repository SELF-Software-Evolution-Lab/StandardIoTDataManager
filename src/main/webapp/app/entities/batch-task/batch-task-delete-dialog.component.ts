import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBatchTask } from 'app/shared/model/batch-task.model';
import { BatchTaskService } from './batch-task.service';

@Component({
    selector: 'jhi-batch-task-delete-dialog',
    templateUrl: './batch-task-delete-dialog.component.html'
})
export class BatchTaskDeleteDialogComponent {
    batchTask: IBatchTask;

    constructor(
        protected batchTaskService: BatchTaskService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.batchTaskService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'batchTaskListModification',
                content: 'Deleted an batchTask'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-batch-task-delete-popup',
    template: ''
})
export class BatchTaskDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ batchTask }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BatchTaskDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.batchTask = batchTask;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/batch-task', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/batch-task', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
