import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    DeviceComponent,
    DeviceDeleteDialogComponent,
    DeviceDeletePopupComponent,
    DeviceDetailComponent,
    devicePopupRoute,
    deviceRoute,
    DeviceUpdateComponent
} from './';

const ENTITY_STATES = [...deviceRoute, ...devicePopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [DeviceComponent, DeviceDetailComponent, DeviceUpdateComponent, DeviceDeleteDialogComponent, DeviceDeletePopupComponent],
    entryComponents: [DeviceComponent, DeviceUpdateComponent, DeviceDeleteDialogComponent, DeviceDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoDeviceModule {}
