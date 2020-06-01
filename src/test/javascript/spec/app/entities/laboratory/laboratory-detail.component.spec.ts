/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { LaboratoryDetailComponent } from 'app/entities/laboratory/laboratory-detail.component';
import { Laboratory } from 'app/shared/model/laboratory.model';

describe('Component Tests', () => {
    describe('Laboratory Management Detail Component', () => {
        let comp: LaboratoryDetailComponent;
        let fixture: ComponentFixture<LaboratoryDetailComponent>;
        const route = ({ data: of({ laboratory: new Laboratory('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [LaboratoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(LaboratoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LaboratoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.laboratory).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
