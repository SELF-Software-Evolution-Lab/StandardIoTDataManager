import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XrepoSharedModule } from 'app/shared';
import {
    ExperimentComponent,
    ExperimentDeleteDialogComponent,
    ExperimentDeletePopupComponent,
    ExperimentDetailComponent,
    experimentPopupRoute,
    experimentRoute,
    ExperimentUpdateComponent
} from './';

const ENTITY_STATES = [...experimentRoute, ...experimentPopupRoute];

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ExperimentComponent,
        ExperimentDetailComponent,
        ExperimentUpdateComponent,
        ExperimentDeleteDialogComponent,
        ExperimentDeletePopupComponent
    ],
    entryComponents: [ExperimentComponent, ExperimentUpdateComponent, ExperimentDeleteDialogComponent, ExperimentDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoExperimentModule {}
