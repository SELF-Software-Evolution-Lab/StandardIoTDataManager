import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISampleFiles } from 'app/shared/model/sample-files.model';
import { SampleFilesService } from './sample-files.service';

@Component({
    selector: 'jhi-sample-files-delete-dialog',
    templateUrl: './sample-files-delete-dialog.component.html'
})
export class SampleFilesDeleteDialogComponent {
    sampleFiles: ISampleFiles;

    constructor(
        protected sampleFilesService: SampleFilesService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.sampleFilesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'sampleFilesListModification',
                content: 'Deleted an sampleFiles'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sample-files-delete-popup',
    template: ''
})
export class SampleFilesDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sampleFiles }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SampleFilesDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.sampleFiles = sampleFiles;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sample-files', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sample-files', { outlets: { popup: null } }]);
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
