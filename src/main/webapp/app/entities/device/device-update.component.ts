import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from './device.service';

@Component({
    selector: 'jhi-device-update',
    templateUrl: './device-update.component.html'
})
export class DeviceUpdateComponent implements OnInit {
    device: IDevice;
    isSaving: boolean;

    constructor(protected deviceService: DeviceService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ device }) => {
            this.device = device;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.device.id !== undefined) {
            this.subscribeToSaveResponse(this.deviceService.update(this.device));
        } else {
            this.subscribeToSaveResponse(this.deviceService.create(this.device));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>) {
        result.subscribe((res: HttpResponse<IDevice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
