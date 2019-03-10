/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { XrepoTestModule } from '../../../test.module';
import { SamplingDeleteDialogComponent } from 'app/entities/sampling/sampling-delete-dialog.component';
import { SamplingService } from 'app/entities/sampling/sampling.service';

describe('Component Tests', () => {
    describe('Sampling Management Delete Component', () => {
        let comp: SamplingDeleteDialogComponent;
        let fixture: ComponentFixture<SamplingDeleteDialogComponent>;
        let service: SamplingService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SamplingDeleteDialogComponent]
            })
                .overrideTemplate(SamplingDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SamplingDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SamplingService);
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
