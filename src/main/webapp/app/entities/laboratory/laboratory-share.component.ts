import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as moment from 'moment';

import { ILaboratory } from 'app/shared/model/laboratory.model';

import { LaboratoryService } from 'app/entities/laboratory/laboratory.service';

@Component({
    selector: 'jhi-laboratory-share',
    templateUrl: './laboratory-share.component.html',
    styles: []
})
export class LaboratoryShareComponent implements OnInit {
    fileShares: String[];
    laboratory: ILaboratory;
    today;

    constructor(protected activatedRoute: ActivatedRoute, protected laboratoryService: LaboratoryService) {}

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

    getFileName(fileUrl: String) {
        let fileURL = fileUrl.split(',')[0].split('/');
        return fileURL[1];
    }

    getFileLocation(fileUrl: String) {
        let fileURL = fileUrl.split(',')[0].split('/');
        return this.laboratoryService.locateResult(fileURL[1], fileURL[2], fileURL[3]);
    }

    getFileType(fileUrl: String) {
        return fileUrl.split(',')[1];
    }
}
