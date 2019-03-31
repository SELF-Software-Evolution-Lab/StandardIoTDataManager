import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'organization',
                loadChildren: './organization/organization.module#XrepoOrganizationModule'
            },
            {
                path: 'target-system',
                loadChildren: './target-system/target-system.module#XrepoTargetSystemModule'
            },
            {
                path: 'experiment',
                loadChildren: './experiment/experiment.module#XrepoExperimentModule'
            },
            {
                path: 'sampling',
                loadChildren: './sampling/sampling.module#XrepoSamplingModule'
            },
            {
                path: 'sample',
                loadChildren: './sample/sample.module#XrepoSampleModule'
            },
            {
                path: 'tag',
                loadChildren: './tag/tag.module#XrepoTagModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoEntityModule {}
