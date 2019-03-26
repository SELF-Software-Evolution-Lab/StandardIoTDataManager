/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { XrepoTestModule } from '../../../test.module';
import { SampleFilesDeleteDialogComponent } from 'app/entities/sample-files/sample-files-delete-dialog.component';
import { SampleFilesService } from 'app/entities/sample-files/sample-files.service';

describe('Component Tests', () => {
    describe('SampleFiles Management Delete Component', () => {
        let comp: SampleFilesDeleteDialogComponent;
        let fixture: ComponentFixture<SampleFilesDeleteDialogComponent>;
        let service: SampleFilesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SampleFilesDeleteDialogComponent]
            })
                .overrideTemplate(SampleFilesDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SampleFilesDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SampleFilesService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete('123');
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith('123');
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
