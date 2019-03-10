/* tslint:disable max-line-length */
import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { map, take } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ExperimentService } from 'app/entities/experiment/experiment.service';
import { Experiment, IExperiment } from 'app/shared/model/experiment.model';

describe('Service Tests', () => {
    describe('Experiment Service', () => {
        let injector: TestBed;
        let service: ExperimentService;
        let httpMock: HttpTestingController;
        let elemDefault: IExperiment;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ExperimentService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Experiment('ID', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        created: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a Experiment', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID',
                        created: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        created: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Experiment(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Experiment', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        description: 'BBBBBB',
                        notes: 'BBBBBB',
                        created: currentDate.format(DATE_TIME_FORMAT),
                        createdBy: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        created: currentDate
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

            it('should return a list of Experiment', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        description: 'BBBBBB',
                        notes: 'BBBBBB',
                        created: currentDate.format(DATE_TIME_FORMAT),
                        createdBy: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        created: currentDate
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

            it('should delete a Experiment', async () => {
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
