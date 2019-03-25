import { Component, EventEmitter, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ISampling } from 'app/shared/model/sampling.model';
import { SamplingService } from './sampling.service';
import { IExperiment } from 'app/shared/model/experiment.model';
import { ExperimentService } from 'app/entities/experiment';
import { TagService } from 'app/entities/tag';
import { ITag } from 'app/shared/model/tag.model';
import { OperativeConditionModalService } from 'app/entities/sampling/operative-condition/operative-condition-modal.service';
import { ISensor } from 'app/shared/model/sensor.model';
import { IOperativeCondition } from 'app/shared/model/operative-condition.model';
import { DeviceModalService } from 'app/entities/sampling/device/device-modal.service';
import { IDevice } from 'app/shared/model/device.model';
import { SensorModalService } from 'app/entities/sampling/sensor/sensor-modal.service';

@Component({
    selector: 'jhi-sampling-update',
    templateUrl: './sampling-update.component.html'
})
export class SamplingUpdateComponent implements OnInit {
    sampling: ISampling;
    isSaving: boolean;

    experiments: IExperiment[];
    startTime: string;
    endTime: string;
    tags: String[] = [];

    conditionModalRes: EventEmitter<IOperativeCondition>;
    deviceModalRes: EventEmitter<IDevice>;
    sensorModalRes: EventEmitter<ISensor>;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected samplingService: SamplingService,
        protected experimentService: ExperimentService,
        protected activatedRoute: ActivatedRoute,
        protected tagService: TagService,
        protected operativeConditionModalService: OperativeConditionModalService,
        protected deviceModalService: DeviceModalService,
        protected sensorModalService: SensorModalService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.loadAllTags();
        this.activatedRoute.data.subscribe(({ sampling }) => {
            this.sampling = sampling;
            this.startTime = this.sampling.startTime != null ? this.sampling.startTime.format(DATE_TIME_FORMAT) : null;
            this.endTime = this.sampling.endTime != null ? this.sampling.endTime.format(DATE_TIME_FORMAT) : null;
            if (!this.sampling.conditions) {
                this.sampling.conditions = [];
            }
            if (!this.sampling.devices) {
                this.sampling.devices = [];
            }
            if (!this.sampling.sensors) {
                this.sampling.sensors = [];
            }
        });
        this.experimentService
            .query({ filter: 'sampling-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IExperiment[]>) => mayBeOk.ok),
                map((response: HttpResponse<IExperiment[]>) => response.body)
            )
            .subscribe(
                (res: IExperiment[]) => {
                    if (!this.sampling.experimentId) {
                        this.experiments = res;
                    } else {
                        this.experimentService
                            .find(this.sampling.experimentId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IExperiment>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IExperiment>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IExperiment) => (this.experiments = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.sampling.startTime = this.startTime != null ? moment(this.startTime, DATE_TIME_FORMAT) : null;
        this.sampling.endTime = this.endTime != null ? moment(this.endTime, DATE_TIME_FORMAT) : null;
        if (this.sampling.id !== undefined) {
            this.subscribeToSaveResponse(this.samplingService.update(this.sampling));
        } else {
            this.subscribeToSaveResponse(this.samplingService.create(this.sampling));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISampling>>) {
        result.subscribe((res: HttpResponse<ISampling>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackExperimentById(index: number, item: IExperiment) {
        return item.id;
    }

    loadAllTags() {
        this.tagService
            .query({})
            .subscribe((res: HttpResponse<ITag[]>) => this.todosTags(res.body), (res: HttpErrorResponse) => this.onError(res.message));
    }

    protected todosTags(data: ITag[]) {
        console.log(data);
        const lista: String[] = [];
        data.forEach(element => {
            lista.push(element.name);
        });
        this.tags = lista;
    }

    addCondition() {
        this.conditionModalRes = this.operativeConditionModalService.openToCreate();
        this.conditionModalRes.subscribe(condition => {
            if (condition) {
                this.sampling.conditions.push(condition);
            }
        });
    }

    editCondition(i: number) {
        this.conditionModalRes = this.operativeConditionModalService.openToEdit(this.sampling.conditions[i]);
        this.conditionModalRes.subscribe(condition => {
            if (condition) {
                this.sampling.conditions[i] = condition;
            }
        });
    }

    removeCondition(i: number) {
        this.sampling.conditions.splice(i, 1);
    }

    addDevice() {
        this.deviceModalRes = this.deviceModalService.openToCreate();
        this.deviceModalRes.subscribe(device => {
            if (device) {
                this.sampling.devices.push(device);
            }
        });
    }

    editDevice(i: number) {
        this.deviceModalRes = this.deviceModalService.openToEdit(this.sampling.devices[i]);
        this.deviceModalRes.subscribe(device => {
            if (device) {
                this.sampling.devices[i] = device;
            }
        });
    }

    removeDevice(i: number) {
        this.sampling.devices.splice(i, 1);
    }

    addSensor() {
        this.sensorModalRes = this.sensorModalService.openToCreate();
        this.sensorModalRes.subscribe(sensor => {
            if (sensor) {
                this.sampling.sensors.push(sensor);
            }
        });
    }

    editSensor(i: number) {
        this.sensorModalRes = this.sensorModalService.openToCreate();
        this.sensorModalRes.subscribe(sensor => {
            if (sensor) {
                this.sampling.sensors[i] = sensor;
            }
        });
    }

    removeSensor(i: number) {
        this.sampling.sensors.splice(i, 1);
    }
}
