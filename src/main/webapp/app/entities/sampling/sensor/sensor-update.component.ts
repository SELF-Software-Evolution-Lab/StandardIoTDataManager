import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { ISensor, Sensor } from 'app/shared/model/sensor.model';

@Component({
    selector: 'jhi-sensor-update',
    templateUrl: './sensor-update.component.html'
})
export class SensorUpdateComponent implements OnInit {
    @Input() sensor: ISensor;
    @Input() mode: string;
    @Output() returnCondition: EventEmitter<any> = new EventEmitter();

    constructor(protected jhiAlertService: JhiAlertService, public activeModal: NgbActiveModal) {}

    ngOnInit() {
        if (!this.sensor) {
            this.sensor = new Sensor();
        }
    }

    save() {
        this.activeModal.dismiss('save sensor');
        this.returnCondition.emit(this.sensor);
    }

    cancel() {
        this.activeModal.dismiss('cancel sensor');
    }
}
