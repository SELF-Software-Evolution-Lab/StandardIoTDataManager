/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { SamplingUpdateComponent } from 'app/entities/sampling/sampling-update.component';
import { SamplingService } from 'app/entities/sampling/sampling.service';
import { Sampling } from 'app/shared/model/sampling.model';

describe('Component Tests', () => {
    describe('Sampling Management Update Component', () => {
        let comp: SamplingUpdateComponent;
        let fixture: ComponentFixture<SamplingUpdateComponent>;
        let service: SamplingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SamplingUpdateComponent]
            })
                .overrideTemplate(SamplingUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SamplingUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SamplingService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Sampling('123');
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.sampling = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Sampling();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.sampling = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
