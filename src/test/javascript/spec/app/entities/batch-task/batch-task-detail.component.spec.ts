/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XrepoTestModule } from '../../../test.module';
import { BatchTaskDetailComponent } from 'app/entities/batch-task/batch-task-detail.component';
import { BatchTask } from 'app/shared/model/batch-task.model';

describe('Component Tests', () => {
    describe('BatchTask Management Detail Component', () => {
        let comp: BatchTaskDetailComponent;
        let fixture: ComponentFixture<BatchTaskDetailComponent>;
        const route = ({ data: of({ batchTask: new BatchTask('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [XrepoTestModule],
                declarations: [BatchTaskDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BatchTaskDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BatchTaskDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.batchTask).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
