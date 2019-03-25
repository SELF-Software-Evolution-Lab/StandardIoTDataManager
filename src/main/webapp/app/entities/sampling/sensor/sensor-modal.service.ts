import { EventEmitter, Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SensorUpdateComponent } from 'app/entities/sampling/sensor/sensor-update.component';
import { ISensor, Sensor } from 'app/shared/model/sensor.model';

@Injectable()
export class SensorModalService {
    private isOpen = false;

    constructor(private modalService: NgbModal) {}

    _open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(SensorUpdateComponent);
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

    openToCreate(): EventEmitter<ISensor> {
        const modalRef = this._open();
        modalRef.componentInstance.mode = 'Add';
        return modalRef.componentInstance.returnCondition;
    }

    openToEdit(sensor: ISensor): EventEmitter<ISensor> {
        const modalRef = this._open();
        modalRef.componentInstance.sensor = new Sensor(sensor.internalId, sensor.sensorType, sensor.potentialFreq, sensor.samplingFreq);
        modalRef.componentInstance.mode = 'Edit';
        return modalRef.componentInstance.returnCondition;
    }
}
