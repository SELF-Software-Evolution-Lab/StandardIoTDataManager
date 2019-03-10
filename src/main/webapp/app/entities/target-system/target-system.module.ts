import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    TargetSystemComponent,
    TargetSystemDetailComponent,
    TargetSystemUpdateComponent,
    TargetSystemDeletePopupComponent,
    TargetSystemDeleteDialogComponent,
    targetSystemRoute,
    targetSystemPopupRoute
} from './';

const ENTITY_STATES = [...targetSystemRoute, ...targetSystemPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TargetSystemComponent,
        TargetSystemDetailComponent,
        TargetSystemUpdateComponent,
        TargetSystemDeleteDialogComponent,
        TargetSystemDeletePopupComponent
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
