import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    BatchTaskComponent,
    BatchTaskDetailComponent,
    BatchTaskUpdateComponent,
    BatchTaskDeletePopupComponent,
    BatchTaskDeleteDialogComponent,
    batchTaskRoute,
    batchTaskPopupRoute
} from './';

const ENTITY_STATES = [...batchTaskRoute, ...batchTaskPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BatchTaskComponent,
        BatchTaskDetailComponent,
        BatchTaskUpdateComponent,
        BatchTaskDeleteDialogComponent,
        BatchTaskDeletePopupComponent
    ],
    entryComponents: [BatchTaskComponent, BatchTaskUpdateComponent, BatchTaskDeleteDialogComponent, BatchTaskDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoBatchTaskModule {}
