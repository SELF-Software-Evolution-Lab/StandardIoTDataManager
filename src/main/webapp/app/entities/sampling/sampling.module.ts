import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    SamplingComponent,
    SamplingDeleteDialogComponent,
    SamplingDeletePopupComponent,
    SamplingDetailComponent,
    samplingPopupRoute,
    samplingRoute,
    SamplingUpdateComponent
} from './';
import { OperativeConditionUpdateComponent } from 'app/entities/sampling/operative-condition/operative-condition-update.component';
import { OperativeConditionModalService } from 'app/entities/sampling/operative-condition/operative-condition-modal.service';
import { DeviceModalService } from 'app/entities/sampling/device/device-modal.service';
import { DeviceUpdateComponent } from 'app/entities/sampling/device/device-update.component';
import { SensorUpdateComponent } from 'app/entities/sampling/sensor/sensor-update.component';
import { SensorModalService } from 'app/entities/sampling/sensor/sensor-modal.service';

const ENTITY_STATES = [...samplingRoute, ...samplingPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SamplingComponent,
        SamplingDetailComponent,
        SamplingUpdateComponent,
        SamplingDeleteDialogComponent,
        SamplingDeletePopupComponent,
        //
        OperativeConditionUpdateComponent,
        DeviceUpdateComponent,
        SensorUpdateComponent
    ],
    entryComponents: [
        SamplingComponent,
        SamplingUpdateComponent,
        SamplingDeleteDialogComponent,
        SamplingDeletePopupComponent,
        //
        OperativeConditionUpdateComponent,
        DeviceUpdateComponent,
        SensorUpdateComponent
    ],
    providers: [OperativeConditionModalService, DeviceModalService, SensorModalService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSamplingModule {}
