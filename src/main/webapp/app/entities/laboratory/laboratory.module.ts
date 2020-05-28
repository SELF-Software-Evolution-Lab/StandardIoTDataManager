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

import { SamplingComponent } from 'app/entities/sampling/sampling.component';
import { SamplingService } from 'app/entities/sampling/sampling.service';

const ENTITY_STATES = [...laboratoryRoute, ...laboratoryPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        LaboratoryComponent,
        LaboratoryDetailComponent,
        LaboratoryUpdateComponent,
        LaboratoryDeleteDialogComponent,
        LaboratoryDeletePopupComponent,
        LaboratoryShareComponent,
        //
        SamplingComponent
    ],
    entryComponents: [
        LaboratoryComponent,
        LaboratoryUpdateComponent,
        LaboratoryDeleteDialogComponent,
        LaboratoryDeletePopupComponent,
        //
        SamplingComponent
    ],
    providers: [SamplingService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoLaboratoryModule {}
