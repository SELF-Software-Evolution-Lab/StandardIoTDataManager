/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { SamplesFilesDetailComponent } from 'app/entities/samples-files/samples-files-detail.component';
import { SamplesFiles } from 'app/shared/model/samples-files.model';

describe('Component Tests', () => {
    describe('SamplesFiles Management Detail Component', () => {
        let comp: SamplesFilesDetailComponent;
        let fixture: ComponentFixture<SamplesFilesDetailComponent>;
        const route = ({ data: of({ samplesFiles: new SamplesFiles('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SamplesFilesDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SamplesFilesDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SamplesFilesDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.samplesFiles).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
