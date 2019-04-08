import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    SamplesFilesComponent,
    SamplesFilesDetailComponent,
    SamplesFilesUpdateComponent,
    SamplesFilesDeletePopupComponent,
    SamplesFilesDeleteDialogComponent,
    samplesFilesRoute,
    samplesFilesPopupRoute
} from './';

const ENTITY_STATES = [...samplesFilesRoute, ...samplesFilesPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SamplesFilesComponent,
        SamplesFilesDetailComponent,
        SamplesFilesUpdateComponent,
        SamplesFilesDeleteDialogComponent,
        SamplesFilesDeletePopupComponent
    ],
    entryComponents: [
        SamplesFilesComponent,
        SamplesFilesUpdateComponent,
        SamplesFilesDeleteDialogComponent,
        SamplesFilesDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSamplesFilesModule {}
