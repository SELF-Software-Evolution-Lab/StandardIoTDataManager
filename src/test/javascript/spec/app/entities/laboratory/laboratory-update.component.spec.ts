/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { LaboratoryUpdateComponent } from 'app/entities/laboratory/laboratory-update.component';
import { LaboratoryService } from 'app/entities/laboratory/laboratory.service';
import { Laboratory } from 'app/shared/model/laboratory.model';

describe('Component Tests', () => {
    describe('Laboratory Management Update Component', () => {
        let comp: LaboratoryUpdateComponent;
        let fixture: ComponentFixture<LaboratoryUpdateComponent>;
        let service: LaboratoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [LaboratoryUpdateComponent]
            })
                .overrideTemplate(LaboratoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(LaboratoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LaboratoryService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Laboratory('123');
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.laboratory = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Laboratory();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.laboratory = entity;
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
