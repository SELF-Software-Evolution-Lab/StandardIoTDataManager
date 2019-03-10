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

const ENTITY_STATES = [...samplingRoute, ...samplingPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SamplingComponent,
        SamplingDetailComponent,
        SamplingUpdateComponent,
        SamplingDeleteDialogComponent,
        SamplingDeletePopupComponent
    ],
    entryComponents: [SamplingComponent, SamplingUpdateComponent, SamplingDeleteDialogComponent, SamplingDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSamplingModule {}
