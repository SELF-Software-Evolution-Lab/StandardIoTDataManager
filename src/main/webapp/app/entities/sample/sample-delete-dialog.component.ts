import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISample } from 'app/shared/model/sample.model';
import { SampleService } from './sample.service';

@Component({
    selector: 'jhi-sample-delete-dialog',
    templateUrl: './sample-delete-dialog.component.html'
})
export class SampleDeleteDialogComponent {
    sample: ISample;

    constructor(protected sampleService: SampleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.sampleService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'sampleListModification',
                content: 'Deleted an sample'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sample-delete-popup',
    template: ''
})
export class SampleDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sample }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SampleDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.sample = sample;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sample', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sample', { outlets: { popup: null } }]);
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
