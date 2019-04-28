import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import { SamplesFilesUpdateComponent, samplesFilesRoute } from './';

const ENTITY_STATES = [...samplesFilesRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SamplesFilesUpdateComponent],
    entryComponents: [SamplesFilesUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSamplesFilesModule {}
