import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    SampleComponent,
    SampleDeleteDialogComponent,
    SampleDeletePopupComponent,
    SampleDetailComponent,
    samplePopupRoute,
    sampleRoute,
    SampleUpdateComponent
} from './';

const ENTITY_STATES = [...sampleRoute, ...samplePopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SampleComponent, SampleDetailComponent, SampleUpdateComponent, SampleDeleteDialogComponent, SampleDeletePopupComponent],
    entryComponents: [SampleComponent, SampleUpdateComponent, SampleDeleteDialogComponent, SampleDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSampleModule {}
