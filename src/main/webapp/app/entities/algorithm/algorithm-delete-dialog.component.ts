import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from './algorithm.service';

@Component({
    selector: 'jhi-algorithm-delete-dialog',
    templateUrl: './algorithm-delete-dialog.component.html'
})
export class AlgorithmDeleteDialogComponent {
    algorithm: IAlgorithm;

    constructor(
        protected algorithmService: AlgorithmService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.algorithmService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'algorithmListModification',
                content: 'Deleted an algorithm'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-algorithm-delete-popup',
    template: ''
})
export class AlgorithmDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ algorithm }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AlgorithmDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.algorithm = algorithm;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/algorithm', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/algorithm', { outlets: { popup: null } }]);
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
