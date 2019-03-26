/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { SampleFilesDetailComponent } from 'app/entities/sample-files/sample-files-detail.component';
import { SampleFiles } from 'app/shared/model/sample-files.model';

describe('Component Tests', () => {
    describe('SampleFiles Management Detail Component', () => {
        let comp: SampleFilesDetailComponent;
        let fixture: ComponentFixture<SampleFilesDetailComponent>;
        const route = ({ data: of({ sampleFiles: new SampleFiles('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SampleFilesDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SampleFilesDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SampleFilesDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.sampleFiles).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
