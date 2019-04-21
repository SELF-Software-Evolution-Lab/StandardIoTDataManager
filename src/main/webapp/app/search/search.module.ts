import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { XrepoSharedModule } from 'app/shared';
import { SearchSampleComponent } from './sample/search-sample.component';
import { SearchLayoutComponent } from 'app/search/layout/search-layout.component';
import { RouterModule } from '@angular/router';
import { SEARCH_ROUTE } from 'app/search/layout/search-layout.route';
import { SearchSampleService } from 'app/search/sample/sample-search.service';

@NgModule({
    imports: [XrepoSharedModule, RouterModule.forChild([SEARCH_ROUTE])],
    declarations: [SearchLayoutComponent, SearchSampleComponent],
    entryComponents: [SearchLayoutComponent],
    providers: [SearchSampleService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class XrepoSearchModule {}
