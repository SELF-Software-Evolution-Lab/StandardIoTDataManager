import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISubSet } from 'app/shared/model/sub-set.model';
import { SubSetService } from './sub-set.service';

@Component({
    selector: 'jhi-sub-set-delete-dialog',
    templateUrl: './sub-set-delete-dialog.component.html'
})
export class SubSetDeleteDialogComponent {
    subSet: ISubSet;

    constructor(protected subSetService: SubSetService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.subSetService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'subSetListModification',
                content: 'Deleted an subSet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sub-set-delete-popup',
    template: ''
})
export class SubSetDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ subSet }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SubSetDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.subSet = subSet;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sub-set', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sub-set', { outlets: { popup: null } }]);
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
