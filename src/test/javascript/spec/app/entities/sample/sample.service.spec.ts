/* tslint:disable max-line-length */
import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { map, take } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SampleService } from 'app/entities/sample/sample.service';
import { ISample, Sample } from 'app/shared/model/sample.model';

describe('Service Tests', () => {
    describe('Sample Service', () => {
        let injector: TestBed;
        let service: SampleService;
        let httpMock: HttpTestingController;
        let elemDefault: ISample;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SampleService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Sample('ID', currentDate, 'AAAAAAA', 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        ts: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a Sample', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID',
                        ts: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        ts: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Sample(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Sample', async () => {
                const returnedFromService = Object.assign(
                    {
                        ts: currentDate.format(DATE_TIME_FORMAT),
                        sensorInternalId: 'BBBBBB',
                        samplingId: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        ts: currentDate
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

            it('should return a list of Sample', async () => {
                const returnedFromService = Object.assign(
                    {
                        ts: currentDate.format(DATE_TIME_FORMAT),
                        sensorInternalId: 'BBBBBB',
                        samplingId: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        ts: currentDate
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

            it('should delete a Sample', async () => {
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
