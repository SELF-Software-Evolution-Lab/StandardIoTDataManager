import { NgModule } from '@angular/core';

import { XrepoSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [XrepoSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [XrepoSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class XrepoSharedCommonModule {}
