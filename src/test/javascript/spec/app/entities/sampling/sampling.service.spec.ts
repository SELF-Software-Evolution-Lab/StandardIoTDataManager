/* tslint:disable max-line-length */
import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { map, take } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SamplingService } from 'app/entities/sampling/sampling.service';
import { ISampling, Sampling } from 'app/shared/model/sampling.model';

describe('Service Tests', () => {
    describe('Sampling Service', () => {
        let injector: TestBed;
        let service: SamplingService;
        let httpMock: HttpTestingController;
        let elemDefault: ISampling;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SamplingService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Sampling('ID', 'AAAAAAA', 'AAAAAAA', currentDate, currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        startTime: currentDate.format(DATE_TIME_FORMAT),
                        endTime: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a Sampling', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID',
                        startTime: currentDate.format(DATE_TIME_FORMAT),
                        endTime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        startTime: currentDate,
                        endTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Sampling(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Sampling', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        notes: 'BBBBBB',
                        startTime: currentDate.format(DATE_TIME_FORMAT),
                        endTime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        startTime: currentDate,
                        endTime: currentDate
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

            it('should return a list of Sampling', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        notes: 'BBBBBB',
                        startTime: currentDate.format(DATE_TIME_FORMAT),
                        endTime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        startTime: currentDate,
                        endTime: currentDate
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

            it('should delete a Sampling', async () => {
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
