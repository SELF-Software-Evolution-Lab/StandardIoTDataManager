import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { ISensor, Sensor } from 'app/shared/model/sensor.model';
import { IDevice } from 'app/shared/model/device.model';

@Component({
    selector: 'jhi-sensor-update',
    templateUrl: './sensor-update.component.html'
})
export class SensorUpdateComponent implements OnInit {
    @Input() sensor: ISensor;
    @Input() devices: IDevice[];
    @Input() mode: string;
    @Output() returnCondition: EventEmitter<any> = new EventEmitter();
    selectedDevice: IDevice;

    constructor(protected jhiAlertService: JhiAlertService, public activeModal: NgbActiveModal) {}

    ngOnInit() {
        if (!this.sensor) {
            this.sensor = new Sensor();
        } else {
            this.selectedDevice = this.devices.filter(value => value.internalId === this.sensor.deviceId).pop();
        }
    }

    save() {
        this.fillDeviceData();
        this.activeModal.dismiss('save sensor');
        this.returnCondition.emit(this.sensor);
    }

    cancel() {
        this.activeModal.dismiss('cancel sensor');
    }

    trackDeviceById(index: number, item: IDevice) {
        return item.internalId;
    }

    fillDeviceData() {
        if (!this.selectedDevice) {
            this.sensor.deviceId = null;
            this.sensor.deviceName = null;
        } else {
            this.sensor.deviceId = this.selectedDevice.internalId;
            this.sensor.deviceName = this.selectedDevice.name;
        }
    }
}
