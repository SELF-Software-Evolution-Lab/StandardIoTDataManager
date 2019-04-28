/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { BatchTaskService } from 'app/entities/batch-task/batch-task.service';
import { IBatchTask, BatchTask, TaskType, TaskState } from 'app/shared/model/batch-task.model';

describe('Service Tests', () => {
    describe('BatchTask Service', () => {
        let injector: TestBed;
        let service: BatchTaskService;
        let httpMock: HttpTestingController;
        let elemDefault: IBatchTask;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(BatchTaskService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new BatchTask('ID', 'AAAAAAA', TaskType.UNDEFINED, TaskState.PENDING, currentDate, currentDate, currentDate, 0);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        createDate: currentDate.format(DATE_TIME_FORMAT),
                        startDate: currentDate.format(DATE_TIME_FORMAT),
                        endDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find('123')
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a BatchTask', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID',
                        createDate: currentDate.format(DATE_TIME_FORMAT),
                        startDate: currentDate.format(DATE_TIME_FORMAT),
                        endDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        createDate: currentDate,
                        startDate: currentDate,
                        endDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new BatchTask(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a BatchTask', async () => {
                const returnedFromService = Object.assign(
                    {
                        description: 'BBBBBB',
                        type: 'BBBBBB',
                        state: 'BBBBBB',
                        createDate: currentDate.format(DATE_TIME_FORMAT),
                        startDate: currentDate.format(DATE_TIME_FORMAT),
                        endDate: currentDate.format(DATE_TIME_FORMAT),
                        progress: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        createDate: currentDate,
                        startDate: currentDate,
                        endDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of BatchTask', async () => {
                const returnedFromService = Object.assign(
                    {
                        description: 'BBBBBB',
                        type: 'BBBBBB',
                        state: 'BBBBBB',
                        createDate: currentDate.format(DATE_TIME_FORMAT),
                        startDate: currentDate.format(DATE_TIME_FORMAT),
                        endDate: currentDate.format(DATE_TIME_FORMAT),
                        progress: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        createDate: currentDate,
                        startDate: currentDate,
                        endDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a BatchTask', async () => {
                const rxPromise = service.delete('123').subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
