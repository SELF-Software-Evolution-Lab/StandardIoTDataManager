import { Component } from '@angular/core';

@Component({
    selector: 'jhi-search-layout',
    templateUrl: './search-layout.component.html'
})
export class SearchLayoutComponent {
    searchModules = [{ name: 'Samples', value: 'SAMPLE' }, { name: 'Experiments', value: 'EXPERIMENT' }];

    onChange($event) {
        console.log($event);
    }
}
