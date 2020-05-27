import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    LaboratoryComponent,
    LaboratoryDetailComponent,
    LaboratoryUpdateComponent,
    LaboratoryDeletePopupComponent,
    LaboratoryDeleteDialogComponent,
    laboratoryRoute,
    laboratoryPopupRoute
} from './';

const ENTITY_STATES = [...laboratoryRoute, ...laboratoryPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        LaboratoryComponent,
        LaboratoryDetailComponent,
        LaboratoryUpdateComponent,
        LaboratoryDeleteDialogComponent,
        LaboratoryDeletePopupComponent
    ],
    entryComponents: [LaboratoryComponent, LaboratoryUpdateComponent, LaboratoryDeleteDialogComponent, LaboratoryDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoLaboratoryModule {}
