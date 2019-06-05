import { EventEmitter, Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IOperativeCondition, OperativeCondition } from 'app/shared/model/operative-condition.model';
import { OperativeConditionUpdateComponent } from 'app/entities/sampling/operative-condition/operative-condition-update.component';
import { Device, IDevice } from 'app/shared/model/device.model';
import { DeviceUpdateComponent } from 'app/entities/sampling/device/device-update.component';

@Injectable()
export class DeviceModalService {
    private isOpen = false;

    constructor(private modalService: NgbModal) {}

    _open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(DeviceUpdateComponent);
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

    openToCreate(): EventEmitter<IDevice> {
        const modalRef = this._open();
        modalRef.componentInstance.mode = 'Add';
        return modalRef.componentInstance.returnCondition;
    }

    openToEdit(device: IDevice): EventEmitter<IDevice> {
        const modalRef = this._open();
        modalRef.componentInstance.device = new Device(device.internalId, device.name, device.description);
        modalRef.componentInstance.mode = 'Edit';
        return modalRef.componentInstance.returnCondition;
    }
}
