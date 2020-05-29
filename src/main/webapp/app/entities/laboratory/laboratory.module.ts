import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    LaboratoryComponent,
    LaboratoryDetailComponent,
    LaboratoryUpdateComponent,
    LaboratoryDeletePopupComponent,
    LaboratoryDeleteDialogComponent,
    LaboratoryShareComponent,
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
        LaboratoryDeletePopupComponent,
        LaboratoryShareComponent
    ],
    entryComponents: [LaboratoryComponent, LaboratoryUpdateComponent, LaboratoryDeleteDialogComponent, LaboratoryDeletePopupComponent],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoLaboratoryModule {}
