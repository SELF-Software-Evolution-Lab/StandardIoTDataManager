import { Route } from '@angular/router';
import { SearchLayoutComponent } from 'app/search/layout/search-layout.component';

export const SEARCH_ROUTE: Route = {
    path: 'search',
    component: SearchLayoutComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Search Engine'
    }
};
