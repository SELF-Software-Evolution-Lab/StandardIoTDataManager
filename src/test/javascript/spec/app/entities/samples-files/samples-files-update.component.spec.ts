/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { SamplesFilesUpdateComponent } from 'app/entities/samples-files/samples-files-update.component';
import { SamplesFilesService } from 'app/entities/samples-files/samples-files.service';
import { SamplesFiles } from 'app/shared/model/samples-files.model';

describe('Component Tests', () => {
    describe('SamplesFiles Management Update Component', () => {
        let comp: SamplesFilesUpdateComponent;
        let fixture: ComponentFixture<SamplesFilesUpdateComponent>;
        let service: SamplesFilesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SamplesFilesUpdateComponent]
            })
                .overrideTemplate(SamplesFilesUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SamplesFilesUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SamplesFilesService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SamplesFiles('123');
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.samplesFiles = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SamplesFiles();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.samplesFiles = entity;
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
