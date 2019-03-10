/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { TargetSystemDetailComponent } from 'app/entities/target-system/target-system-detail.component';
import { TargetSystem } from 'app/shared/model/target-system.model';

describe('Component Tests', () => {
    describe('TargetSystem Management Detail Component', () => {
        let comp: TargetSystemDetailComponent;
        let fixture: ComponentFixture<TargetSystemDetailComponent>;
        const route = ({ data: of({ targetSystem: new TargetSystem('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [TargetSystemDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TargetSystemDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TargetSystemDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.targetSystem).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
