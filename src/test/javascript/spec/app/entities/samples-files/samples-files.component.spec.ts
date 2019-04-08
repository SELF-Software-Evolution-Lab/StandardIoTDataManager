/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { XrepoTestModule } from '../../../test.module';
import { SamplesFilesComponent } from 'app/entities/samples-files/samples-files.component';
import { SamplesFilesService } from 'app/entities/samples-files/samples-files.service';
import { SamplesFiles } from 'app/shared/model/samples-files.model';

describe('Component Tests', () => {
    describe('SamplesFiles Management Component', () => {
        let comp: SamplesFilesComponent;
        let fixture: ComponentFixture<SamplesFilesComponent>;
        let service: SamplesFilesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SamplesFilesComponent],
                providers: []
            })
                .overrideTemplate(SamplesFilesComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SamplesFilesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SamplesFilesService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SamplesFiles('123')],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.samplesFiles[0]).toEqual(jasmine.objectContaining({ id: '123' }));
        });
    });
});
