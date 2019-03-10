/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { XrepoTestModule } from '../../../test.module';
import { TargetSystemDeleteDialogComponent } from 'app/entities/target-system/target-system-delete-dialog.component';
import { TargetSystemService } from 'app/entities/target-system/target-system.service';

describe('Component Tests', () => {
    describe('TargetSystem Management Delete Component', () => {
        let comp: TargetSystemDeleteDialogComponent;
        let fixture: ComponentFixture<TargetSystemDeleteDialogComponent>;
        let service: TargetSystemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [TargetSystemDeleteDialogComponent]
            })
                .overrideTemplate(TargetSystemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TargetSystemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TargetSystemService);
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
