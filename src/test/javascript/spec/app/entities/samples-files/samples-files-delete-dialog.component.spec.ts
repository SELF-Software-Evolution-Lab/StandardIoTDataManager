/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { XrepoTestModule } from '../../../test.module';
import { SamplesFilesDeleteDialogComponent } from 'app/entities/samples-files/samples-files-delete-dialog.component';
import { SamplesFilesService } from 'app/entities/samples-files/samples-files.service';

describe('Component Tests', () => {
    describe('SamplesFiles Management Delete Component', () => {
        let comp: SamplesFilesDeleteDialogComponent;
        let fixture: ComponentFixture<SamplesFilesDeleteDialogComponent>;
        let service: SamplesFilesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SamplesFilesDeleteDialogComponent]
            })
                .overrideTemplate(SamplesFilesDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SamplesFilesDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SamplesFilesService);
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
