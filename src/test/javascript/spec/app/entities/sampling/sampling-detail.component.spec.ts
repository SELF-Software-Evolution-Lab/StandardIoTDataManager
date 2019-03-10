/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { SamplingDetailComponent } from 'app/entities/sampling/sampling-detail.component';
import { Sampling } from 'app/shared/model/sampling.model';

describe('Component Tests', () => {
    describe('Sampling Management Detail Component', () => {
        let comp: SamplingDetailComponent;
        let fixture: ComponentFixture<SamplingDetailComponent>;
        const route = ({ data: of({ sampling: new Sampling('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SamplingDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SamplingDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SamplingDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.sampling).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
