import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { Device, IDevice } from 'app/shared/model/device.model';

@Component({
    selector: 'jhi-device-update',
    templateUrl: './device-update.component.html'
})
export class DeviceUpdateComponent implements OnInit {
    @Input() device: IDevice;
    @Input() mode: string;
    @Output() returnCondition: EventEmitter<any> = new EventEmitter();

    constructor(protected jhiAlertService: JhiAlertService, public activeModal: NgbActiveModal) {}

    ngOnInit() {
        if (!this.device) {
            this.device = new Device();
        }
    }

    save() {
        this.activeModal.dismiss('save device');
        this.returnCondition.emit(this.device);
    }

    cancel() {
        this.activeModal.dismiss('cancel device');
    }
}
