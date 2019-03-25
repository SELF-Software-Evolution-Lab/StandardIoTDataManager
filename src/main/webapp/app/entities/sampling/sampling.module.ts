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
import { OperativeConditionUpdateComponent } from 'app/entities/sampling/operative-condition-update.component';
import { OperativeConditionModalService } from 'app/entities/sampling/operative-condition-modal.service';

const ENTITY_STATES = [...samplingRoute, ...samplingPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SamplingComponent,
        SamplingDetailComponent,
        SamplingUpdateComponent,
        SamplingDeleteDialogComponent,
        SamplingDeletePopupComponent,
        OperativeConditionUpdateComponent
    ],
    entryComponents: [SamplingComponent, SamplingUpdateComponent, SamplingDeleteDialogComponent, SamplingDeletePopupComponent],
    providers: [OperativeConditionModalService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSamplingModule {}
