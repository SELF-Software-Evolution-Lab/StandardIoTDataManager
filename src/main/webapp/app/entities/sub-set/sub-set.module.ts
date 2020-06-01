import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    SubSetComponent,
    SubSetDetailComponent,
    SubSetUpdateComponent,
    SubSetDeletePopupComponent,
    SubSetDeleteDialogComponent,
    subSetRoute,
    subSetPopupRoute
} from './';

const ENTITY_STATES = [...subSetRoute, ...subSetPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SubSetComponent, SubSetDetailComponent, SubSetUpdateComponent, SubSetDeleteDialogComponent, SubSetDeletePopupComponent],
    entryComponents: [SubSetComponent, SubSetUpdateComponent, SubSetDeleteDialogComponent, SubSetDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSubSetModule {}
