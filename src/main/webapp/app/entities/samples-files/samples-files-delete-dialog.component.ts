import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISamplesFiles } from 'app/shared/model/samples-files.model';
import { SamplesFilesService } from './samples-files.service';

@Component({
    selector: 'jhi-samples-files-delete-dialog',
    templateUrl: './samples-files-delete-dialog.component.html'
})
export class SamplesFilesDeleteDialogComponent {
    samplesFiles: ISamplesFiles;

    constructor(
        protected samplesFilesService: SamplesFilesService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.samplesFilesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'samplesFilesListModification',
                content: 'Deleted an samplesFiles'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-samples-files-delete-popup',
    template: ''
})
export class SamplesFilesDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ samplesFiles }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SamplesFilesDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.samplesFiles = samplesFiles;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/samples-files', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/samples-files', { outlets: { popup: null } }]);
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
