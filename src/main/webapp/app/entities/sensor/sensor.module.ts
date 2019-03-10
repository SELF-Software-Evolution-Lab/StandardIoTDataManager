import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    SensorComponent,
    SensorDeleteDialogComponent,
    SensorDeletePopupComponent,
    SensorDetailComponent,
    sensorPopupRoute,
    sensorRoute,
    SensorUpdateComponent
} from './';

const ENTITY_STATES = [...sensorRoute, ...sensorPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SensorComponent, SensorDetailComponent, SensorUpdateComponent, SensorDeleteDialogComponent, SensorDeletePopupComponent],
    entryComponents: [SensorComponent, SensorUpdateComponent, SensorDeleteDialogComponent, SensorDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSensorModule {}
