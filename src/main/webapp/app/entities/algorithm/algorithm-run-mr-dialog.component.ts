import { Component, OnInit, OnDestroy } from '@angular/core';
import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from 'app/entities/algorithm/algorithm.service';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'jhi-algorithm-run-mr-dialog',
    templateUrl: './algorithm-run-mr-dialog.component.html',
    styles: []
})
export class AlgorithmRunMRDialogComponent {
    algorithm: IAlgorithm;

    constructor(
        protected algorithmService: AlgorithmService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmRun(id: string) {
        this.algorithmService.runMR(id).subscribe(response => {
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
export class AlgorithmRunMRPopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ algorithm }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AlgorithmRunMRDialogComponent as Component, { size: 'lg', backdrop: 'static' });
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
