/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { XrepoTestModule } from '../../../test.module';
import { SubSetDeleteDialogComponent } from 'app/entities/sub-set/sub-set-delete-dialog.component';
import { SubSetService } from 'app/entities/sub-set/sub-set.service';

describe('Component Tests', () => {
    describe('SubSet Management Delete Component', () => {
        let comp: SubSetDeleteDialogComponent;
        let fixture: ComponentFixture<SubSetDeleteDialogComponent>;
        let service: SubSetService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [SubSetDeleteDialogComponent]
            })
                .overrideTemplate(SubSetDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SubSetDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubSetService);
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
