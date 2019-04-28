/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { BatchTaskUpdateComponent } from 'app/entities/batch-task/batch-task-update.component';
import { BatchTaskService } from 'app/entities/batch-task/batch-task.service';
import { BatchTask } from 'app/shared/model/batch-task.model';

describe('Component Tests', () => {
    describe('BatchTask Management Update Component', () => {
        let comp: BatchTaskUpdateComponent;
        let fixture: ComponentFixture<BatchTaskUpdateComponent>;
        let service: BatchTaskService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [BatchTaskUpdateComponent]
            })
                .overrideTemplate(BatchTaskUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BatchTaskUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BatchTaskService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new BatchTask('123');
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.batchTask = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new BatchTask();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.batchTask = entity;
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
