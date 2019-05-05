import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { TargetSystemService } from 'app/entities/target-system';
import { ITargetSystem } from 'app/shared/model/target-system.model';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { Observable, of } from 'rxjs';
import { TagService } from 'app/entities/tag';
import { SearchSampleService } from 'app/search/sample/sample-search.service';
import { SampleSearchParameters } from 'app/shared/model/sample-search-parameters.model';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SamplingService } from 'app/entities/sampling';
import { ISensor, SelectableSensor } from 'app/shared/model/sensor.model';

@Component({
    selector: 'jhi-search-sample',
    templateUrl: './search-sample.component.html'
})
export class SearchSampleComponent implements OnInit {
    targetSystems: Observable<ITargetSystem[]>;
    tags: string[];
    operativeConditions: string[];
    sensors: SelectableSensor[];

    selectedTargetSystem: ITargetSystem;
    selectedTags: string[];
    selectedSensors: string[];

    selectedFromDate: string;
    selectedToDate: string;

    selectedOpCondition: any;

    opCondDisabled = true;
    sensorsDisabled = true;
    tagsDisabled = true;

    searchParameters: SampleSearchParameters;
    searchReturned = false;
    searchResults: Number = 0;
    batchTaskId = '';

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected targetSystemService: TargetSystemService,
        protected tagService: TagService,
        protected samplingService: SamplingService,
        protected searchSampleService: SearchSampleService
    ) {}

    ngOnInit() {
        this.searchParameters = new SampleSearchParameters();
        this.targetSystemService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ITargetSystem[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITargetSystem[]>) => response.body)
            )
            .subscribe((res: ITargetSystem[]) => (this.targetSystems = of(res)), (res: HttpErrorResponse) => this.onError(res.message));
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onChangeTargetSystem(selected: ITargetSystem) {
        console.log('selected system ', selected);
        this.operativeConditions = Array.from(new Set(selected.operativeRanges.map(value => value.varName)));

        this.queryTags(selected.id);
        this.querySensors(selected.id);
    }

    private queryTags(tsId: string) {
        this.tagService
            .listByTargetSystem(tsId)
            .pipe(
                filter((mayBeOk: HttpResponse<string[]>) => mayBeOk.ok),
                map((response: HttpResponse<string[]>) => response.body)
            )
            .subscribe(
                (res: string[]) => {
                    this.tags = res;
                    this.tagsDisabled = false;
                    console.log('Found tags', res);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    private querySensors(tsId: string) {
        this.samplingService
            .listSensorsByTargetSystem(tsId)
            .pipe(
                filter((mayBeOk: HttpResponse<ISensor[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISensor[]>) => response.body)
            )
            .subscribe(
                (res: ISensor[]) => {
                    this.sensors = [];
                    for (const sensor of res) {
                        this.sensors.push(new SelectableSensor(sensor.internalId, sensor.sensorType));
                    }
                    this.sensorsDisabled = false;
                    console.log('Found sensors', res);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    onChangeOperativeCondition(selected: string) {
        console.log('selected operative condition ', selected);
    }

    search() {
        this.searchParameters.targetSystemId = [this.selectedTargetSystem.id];
        this.searchParameters.tags = this.selectedTags;
        this.searchParameters.sensorsId = this.selectedSensors;
        this.searchParameters.fromDateTime = this.selectedFromDate != null ? moment(this.selectedFromDate, DATE_TIME_FORMAT) : null;
        this.searchParameters.toDateTime = this.selectedToDate != null ? moment(this.selectedToDate, DATE_TIME_FORMAT) : null;
        this.searchSampleService
            .search(this.searchParameters)
            .subscribe(
                (res: HttpResponse<Array<string>>) => this.onSearchSuccess(res),
                (res: HttpErrorResponse) => this.onSearchError(res)
            );
    }

    private onSearchSuccess(res: HttpResponse<Array<string>>) {
        this.searchReturned = true;
        this.searchResults = res.body['count'];
        this.batchTaskId = res.body['batchTaskId'];
    }

    private onSearchError(res: HttpErrorResponse) {
        this.searchReturned = false;
        console.log('search error ', res);
    }

    tagListPlaceHolder() {
        if (this.tagsDisabled) {
            return '';
        } else if (this.tags.length === 0) {
            return 'No tags found for selected Target System';
        } else {
            return 'Select one or more tags';
        }
    }

    sensorListPlaceHolder() {
        if (this.sensorsDisabled) {
            return '';
        } else if (this.sensors.length === 0) {
            return 'No sensors found for selected Target System';
        } else {
            return 'Select one or more sensors';
        }
    }
}
