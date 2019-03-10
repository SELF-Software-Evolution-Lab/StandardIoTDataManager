/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { TargetSystemUpdateComponent } from 'app/entities/target-system/target-system-update.component';
import { TargetSystemService } from 'app/entities/target-system/target-system.service';
import { TargetSystem } from 'app/shared/model/target-system.model';

describe('Component Tests', () => {
    describe('TargetSystem Management Update Component', () => {
        let comp: TargetSystemUpdateComponent;
        let fixture: ComponentFixture<TargetSystemUpdateComponent>;
        let service: TargetSystemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [TargetSystemUpdateComponent]
            })
                .overrideTemplate(TargetSystemUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TargetSystemUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TargetSystemService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new TargetSystem('123');
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.targetSystem = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new TargetSystem();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.targetSystem = entity;
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
