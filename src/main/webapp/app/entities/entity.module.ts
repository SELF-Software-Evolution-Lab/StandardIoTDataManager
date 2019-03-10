import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'organization',
                loadChildren: './organization/organization.module#XrepoOrganizationModule'
            },
            {
                path: 'target-system',
                loadChildren: './target-system/target-system.module#XrepoTargetSystemModule'
            },
            {
                path: 'sensor',
                loadChildren: './sensor/sensor.module#XrepoSensorModule'
            },
            {
                path: 'device',
                loadChildren: './device/device.module#XrepoDeviceModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoEntityModule {}
