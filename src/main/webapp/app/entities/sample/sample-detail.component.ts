import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { HttpResponse } from '@angular/common/http';
import { ISample } from 'app/shared/model/sample.model';
import { ISampling, Sampling } from 'app/shared/model/sampling.model';
import { SamplingService } from 'app/entities/sampling';
import { filter, map } from 'rxjs/operators';
import { ISensor, Sensor } from 'app/shared/model/sensor.model';

@Component({
    selector: 'jhi-sample-detail',
    templateUrl: './sample-detail.component.html'
})
export class SampleDetailComponent implements OnInit {
    keys = Object.keys;
    sample: ISample;
    sampling: ISampling;
    sensor: ISensor;

    constructor(protected activatedRoute: ActivatedRoute, protected samplingService: SamplingService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sample }) => {
            this.sample = sample;
            this.sampling = new Sampling();
            this.sensor = new Sensor();
            if (this.sample.samplingId) {
                this.samplingService
                    .find(this.sample.samplingId)
                    .pipe(
                        filter((response: HttpResponse<Sampling>) => response.ok),
                        map((sampling: HttpResponse<Sampling>) => sampling.body)
                    )
                    .subscribe(sampling => {
                        this.initSampling(sampling);
                    });
            }
        });
    }

    previousState() {
        window.history.back();
    }

    initSampling(sampling: ISampling) {
        if (sampling) {
            this.sampling = sampling;
            this.sensor = this.sampling.sensors.find(sensor => sensor.internalId === this.sample.sensorInternalId);
        }
    }
}
