import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITargetSystem } from 'app/shared/model/target-system.model';
import { TargetSystemService } from './target-system.service';

@Component({
    selector: 'jhi-target-system-delete-dialog',
    templateUrl: './target-system-delete-dialog.component.html'
})
export class TargetSystemDeleteDialogComponent {
    targetSystem: ITargetSystem;

    constructor(
        protected targetSystemService: TargetSystemService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.targetSystemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'targetSystemListModification',
                content: 'Deleted an targetSystem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-target-system-delete-popup',
    template: ''
})
export class TargetSystemDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ targetSystem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TargetSystemDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.targetSystem = targetSystem;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/target-system', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/target-system', { outlets: { popup: null } }]);
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
