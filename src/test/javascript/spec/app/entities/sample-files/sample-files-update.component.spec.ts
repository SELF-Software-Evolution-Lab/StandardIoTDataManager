/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { SampleFilesUpdateComponent } from 'app/entities/sample-files/sample-files-update.component';
import { SampleFilesService } from 'app/entities/sample-files/sample-files.service';
import { SampleFiles } from 'app/shared/model/sample-files.model';

describe('Component Tests', () => {
    describe('SampleFiles Management Update Component', () => {
        let comp: SampleFilesUpdateComponent;
        let fixture: ComponentFixture<SampleFilesUpdateComponent>;
        let service: SampleFilesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SampleFilesUpdateComponent]
            })
                .overrideTemplate(SampleFilesUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SampleFilesUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SampleFilesService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SampleFiles('123');
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.sampleFiles = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SampleFiles();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.sampleFiles = entity;
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
