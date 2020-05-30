/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { SubSetDetailComponent } from 'app/entities/sub-set/sub-set-detail.component';
import { SubSet } from 'app/shared/model/sub-set.model';

describe('Component Tests', () => {
    describe('SubSet Management Detail Component', () => {
        let comp: SubSetDetailComponent;
        let fixture: ComponentFixture<SubSetDetailComponent>;
        const route = ({ data: of({ subSet: new SubSet('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SubSetDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SubSetDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SubSetDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.subSet).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
