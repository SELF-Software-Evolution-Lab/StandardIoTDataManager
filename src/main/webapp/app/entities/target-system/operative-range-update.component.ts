import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';
import { IOperativeRange, OperativeRange } from 'app/shared/model/operative-range.model';
import { OperativeRangeService } from 'app/entities/target-system/operative-range.service';

@Component({
    selector: 'jhi-operative-range-update',
    templateUrl: './operative-range-update.component.html'
})
export class OperativeRangeUpdateComponent implements OnInit {
    operativeRange: IOperativeRange;

    isSaving: boolean;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected operativeRangeService: OperativeRangeService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        const toModify = this.operativeRangeService.toModify;
        if (!toModify) {
            this.operativeRange = new OperativeRange();
        } else {
            this.operativeRange = new OperativeRange(toModify.varName, toModify.unit, toModify.minVal, toModify.maxVal);
        }
    }

    previousState() {
        this.operativeRangeService.reset();
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.operativeRangeService.newValue = this.operativeRange;
        window.history.back();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
