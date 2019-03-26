import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    SampleFilesComponent,
    SampleFilesDetailComponent,
    SampleFilesUpdateComponent,
    SampleFilesDeletePopupComponent,
    SampleFilesDeleteDialogComponent,
    sampleFilesRoute,
    sampleFilesPopupRoute
} from './';

const ENTITY_STATES = [...sampleFilesRoute, ...sampleFilesPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SampleFilesComponent,
        SampleFilesDetailComponent,
        SampleFilesUpdateComponent,
        SampleFilesDeleteDialogComponent,
        SampleFilesDeletePopupComponent
    ],
    entryComponents: [SampleFilesComponent, SampleFilesUpdateComponent, SampleFilesDeleteDialogComponent, SampleFilesDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSampleFilesModule {}
