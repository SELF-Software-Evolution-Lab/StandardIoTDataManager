import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as moment from 'moment';

import { ILaboratory } from 'app/shared/model/laboratory.model';

@Component({
    selector: 'jhi-laboratory-share',
    templateUrl: './laboratory-share.component.html',
    styles: []
})
export class LaboratoryShareComponent implements OnInit {
    fileShares: String[];
    laboratory: ILaboratory;
    today;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ files, laboratory }) => {
            this.fileShares = files;
            this.laboratory = laboratory;
        });
        this.today = moment();
    }

    linkHasExpired() {
        if (this.laboratory.shareValidThru) {
            return this.laboratory.shareValidThru.isBefore(this.today);
        }
        return true;
    }

    getFileCount() {
        return this.fileShares.length;
    }
}
