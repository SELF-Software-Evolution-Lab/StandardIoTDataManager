import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ISensor } from 'app/shared/model/sensor.model';
import { SensorService } from './sensor.service';

@Component({
    selector: 'jhi-sensor-update',
    templateUrl: './sensor-update.component.html'
})
export class SensorUpdateComponent implements OnInit {
    sensor: ISensor;
    isSaving: boolean;

    constructor(protected sensorService: SensorService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sensor }) => {
            this.sensor = sensor;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.sensor.id !== undefined) {
            this.subscribeToSaveResponse(this.sensorService.update(this.sensor));
        } else {
            this.subscribeToSaveResponse(this.sensorService.create(this.sensor));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISensor>>) {
        result.subscribe((res: HttpResponse<ISensor>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
