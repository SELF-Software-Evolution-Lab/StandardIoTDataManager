/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { SubSetUpdateComponent } from 'app/entities/sub-set/sub-set-update.component';
import { SubSetService } from 'app/entities/sub-set/sub-set.service';
import { SubSet } from 'app/shared/model/sub-set.model';

describe('Component Tests', () => {
    describe('SubSet Management Update Component', () => {
        let comp: SubSetUpdateComponent;
        let fixture: ComponentFixture<SubSetUpdateComponent>;
        let service: SubSetService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SubSetUpdateComponent]
            })
                .overrideTemplate(SubSetUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SubSetUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubSetService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SubSet('123');
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.subSet = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SubSet();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.subSet = entity;
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
