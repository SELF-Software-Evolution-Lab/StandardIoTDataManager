import { EventEmitter, Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { OperativeRangeUpdateComponent } from 'app/entities/target-system/operative-range-update.component';
import { IOperativeRange, OperativeRange } from 'app/shared/model/operative-range.model';

@Injectable()
export class OperativeRangeModalService {
    private isOpen = false;

    constructor(private modalService: NgbModal) {}

    _open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(OperativeRangeUpdateComponent);
        modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return modalRef;
    }

    openToCreate(): EventEmitter<IOperativeRange> {
        const modalRef = this._open();
        modalRef.componentInstance.mode = 'Add ';
        return modalRef.componentInstance.returnRange;
    }

    openToEdit(operativeRange: IOperativeRange): EventEmitter<IOperativeRange> {
        const modalRef = this._open();
        modalRef.componentInstance.operativeRange = new OperativeRange(
            operativeRange.varName,
            operativeRange.unit,
            operativeRange.minVal,
            operativeRange.maxVal
        );
        modalRef.componentInstance.mode = 'Edit ';
        return modalRef.componentInstance.returnRange;
    }
}
