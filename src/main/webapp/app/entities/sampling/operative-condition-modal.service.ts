import { EventEmitter, Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IOperativeCondition, OperativeCondition } from 'app/shared/model/operative-condition.model';
import { OperativeConditionUpdateComponent } from 'app/entities/sampling/operative-condition-update.component';

@Injectable()
export class OperativeConditionModalService {
    private isOpen = false;

    constructor(private modalService: NgbModal) {}

    _open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(OperativeConditionUpdateComponent);
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

    openToCreate(): EventEmitter<IOperativeCondition> {
        const modalRef = this._open();
        modalRef.componentInstance.mode = 'Add';
        return modalRef.componentInstance.returnCondition;
    }

    openToEdit(operativeCondition: IOperativeCondition): EventEmitter<IOperativeCondition> {
        const modalRef = this._open();
        modalRef.componentInstance.operativeCondition = new OperativeCondition(
            operativeCondition.varName,
            operativeCondition.unit,
            operativeCondition.value
        );
        modalRef.componentInstance.mode = 'Edit';
        return modalRef.componentInstance.returnCondition;
    }
}
