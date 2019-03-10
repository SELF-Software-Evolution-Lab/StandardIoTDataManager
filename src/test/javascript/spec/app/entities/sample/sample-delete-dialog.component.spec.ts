/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { XrepoTestModule } from '../../../test.module';
import { SampleDeleteDialogComponent } from 'app/entities/sample/sample-delete-dialog.component';
import { SampleService } from 'app/entities/sample/sample.service';

describe('Component Tests', () => {
    describe('Sample Management Delete Component', () => {
        let comp: SampleDeleteDialogComponent;
        let fixture: ComponentFixture<SampleDeleteDialogComponent>;
        let service: SampleService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SampleDeleteDialogComponent]
            })
                .overrideTemplate(SampleDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SampleDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SampleService);
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
