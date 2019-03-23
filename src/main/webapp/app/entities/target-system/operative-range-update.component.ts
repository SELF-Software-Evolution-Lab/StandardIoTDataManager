import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { IOperativeRange, OperativeRange } from 'app/shared/model/operative-range.model';

@Component({
    selector: 'jhi-operative-range-update',
    templateUrl: './operative-range-update.component.html'
})
export class OperativeRangeUpdateComponent implements OnInit {
    @Input() operativeRange: IOperativeRange;
    @Input() mode: string;
    @Output() returnRange: EventEmitter<any> = new EventEmitter();

    constructor(protected jhiAlertService: JhiAlertService, public activeModal: NgbActiveModal) {}

    ngOnInit() {
        if (!this.operativeRange) {
            this.operativeRange = new OperativeRange();
        }
    }

    save() {
        this.activeModal.dismiss('save operative range');
        this.returnRange.emit(this.operativeRange);
    }

    cancel() {
        this.activeModal.dismiss('cancel operative range');
    }
}
