import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILaboratory } from 'app/shared/model/laboratory.model';
import { LaboratoryService } from './laboratory.service';

@Component({
    selector: 'jhi-laboratory-delete-dialog',
    templateUrl: './laboratory-delete-dialog.component.html'
})
export class LaboratoryDeleteDialogComponent {
    laboratory: ILaboratory;

    constructor(
        protected laboratoryService: LaboratoryService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.laboratoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'laboratoryListModification',
                content: 'Deleted an laboratory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-laboratory-delete-popup',
    template: ''
})
export class LaboratoryDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ laboratory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(LaboratoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.laboratory = laboratory;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/laboratory', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/laboratory', { outlets: { popup: null } }]);
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
