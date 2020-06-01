import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    AlgorithmComponent,
    AlgorithmDetailComponent,
    AlgorithmUpdateComponent,
    AlgorithmDeletePopupComponent,
    AlgorithmDeleteDialogComponent,
    algorithmRoute,
    algorithmPopupRoute,
    AlgorithmRunMRDialogComponent,
    AlgorithmRunMRPopupComponent
} from './';

const ENTITY_STATES = [...algorithmRoute, ...algorithmPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AlgorithmComponent,
        AlgorithmDetailComponent,
        AlgorithmUpdateComponent,
        AlgorithmDeleteDialogComponent,
        AlgorithmDeletePopupComponent,
        AlgorithmRunMRDialogComponent,
        AlgorithmRunMRPopupComponent
    ],
    entryComponents: [
        AlgorithmComponent,
        AlgorithmUpdateComponent,
        AlgorithmDeleteDialogComponent,
        AlgorithmDeletePopupComponent,
        AlgorithmRunMRDialogComponent,
        AlgorithmRunMRPopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoAlgorithmModule {}
