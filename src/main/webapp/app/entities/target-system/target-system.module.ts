import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    TargetSystemComponent,
    TargetSystemDeleteDialogComponent,
    TargetSystemDeletePopupComponent,
    TargetSystemDetailComponent,
    targetSystemPopupRoute,
    targetSystemRoute,
    TargetSystemUpdateComponent
} from './';
import { OperativeRangeUpdateComponent } from 'app/entities/target-system/operative-range-update.component';

const ENTITY_STATES = [...targetSystemRoute, ...targetSystemPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TargetSystemComponent,
        TargetSystemDetailComponent,
        TargetSystemUpdateComponent,
        TargetSystemDeleteDialogComponent,
        TargetSystemDeletePopupComponent,
        OperativeRangeUpdateComponent
    ],
    entryComponents: [
        TargetSystemComponent,
        TargetSystemUpdateComponent,
        TargetSystemDeleteDialogComponent,
        TargetSystemDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoTargetSystemModule {}
