import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { IOperativeCondition, OperativeCondition } from 'app/shared/model/operative-condition.model';

@Component({
    selector: 'jhi-operative-condition-update',
    templateUrl: './operative-condition-update.component.html'
})
export class OperativeConditionUpdateComponent implements OnInit {
    @Input() operativeCondition: IOperativeCondition;
    @Input() mode: string;
    @Output() returnCondition: EventEmitter<any> = new EventEmitter();

    constructor(protected jhiAlertService: JhiAlertService, public activeModal: NgbActiveModal) {}

    ngOnInit() {
        if (!this.operativeCondition) {
            this.operativeCondition = new OperativeCondition();
        }
    }

    save() {
        this.activeModal.dismiss('save operative range');
        this.returnCondition.emit(this.operativeCondition);
    }

    cancel() {
        this.activeModal.dismiss('cancel operative range');
    }
}
