/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { XrepoTestModule } from '../../../test.module';
import { SampleFilesComponent } from 'app/entities/sample-files/sample-files.component';
import { SampleFilesService } from 'app/entities/sample-files/sample-files.service';
import { SampleFiles } from 'app/shared/model/sample-files.model';

describe('Component Tests', () => {
    describe('SampleFiles Management Component', () => {
        let comp: SampleFilesComponent;
        let fixture: ComponentFixture<SampleFilesComponent>;
        let service: SampleFilesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SampleFilesComponent],
                providers: []
            })
                .overrideTemplate(SampleFilesComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SampleFilesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SampleFilesService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SampleFiles('123')],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.sampleFiles[0]).toEqual(jasmine.objectContaining({ id: '123' }));
        });
    });
});
