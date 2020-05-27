import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILaboratory } from 'app/shared/model/laboratory.model';

@Component({
    selector: 'jhi-laboratory-detail',
    templateUrl: './laboratory-detail.component.html'
})
export class LaboratoryDetailComponent implements OnInit {
    laboratory: ILaboratory;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ laboratory }) => {
            this.laboratory = laboratory;
        });
    }

    previousState() {
        window.history.back();
    }
}
