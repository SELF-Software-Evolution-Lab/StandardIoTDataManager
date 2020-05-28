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
            },
            {
                path: 'samples-files',
                loadChildren: './samples-files/samples-files.module#XrepoSamplesFilesModule'
            },
            {
                path: 'batch-task',
                loadChildren: './batch-task/batch-task.module#XrepoBatchTaskModule'
            },
            {
                path: 'laboratory',
                loadChildren: './laboratory/laboratory.module#XrepoLaboratoryModule'
            },
            {
                path: 'algorithm',
                loadChildren: './algorithm/algorithm.module#XrepoAlgorithmModule'
            },
            {
                path: 'sub-set',
                loadChildren: './sub-set/sub-set.module#XrepoSubSetModule'
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
