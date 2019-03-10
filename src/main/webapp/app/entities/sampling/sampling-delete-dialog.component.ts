import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISampling } from 'app/shared/model/sampling.model';
import { SamplingService } from './sampling.service';

@Component({
    selector: 'jhi-sampling-delete-dialog',
    templateUrl: './sampling-delete-dialog.component.html'
})
export class SamplingDeleteDialogComponent {
    sampling: ISampling;

    constructor(protected samplingService: SamplingService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.samplingService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'samplingListModification',
                content: 'Deleted an sampling'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sampling-delete-popup',
    template: ''
})
export class SamplingDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sampling }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SamplingDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.sampling = sampling;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sampling', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sampling', { outlets: { popup: null } }]);
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
