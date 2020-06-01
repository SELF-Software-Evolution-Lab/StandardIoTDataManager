/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { XrepoTestModule } from '../../../test.module';
import { LaboratoryDeleteDialogComponent } from 'app/entities/laboratory/laboratory-delete-dialog.component';
import { LaboratoryService } from 'app/entities/laboratory/laboratory.service';

describe('Component Tests', () => {
    describe('Laboratory Management Delete Component', () => {
        let comp: LaboratoryDeleteDialogComponent;
        let fixture: ComponentFixture<LaboratoryDeleteDialogComponent>;
        let service: LaboratoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [LaboratoryDeleteDialogComponent]
            })
                .overrideTemplate(LaboratoryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LaboratoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LaboratoryService);
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
