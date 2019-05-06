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
import { IOperativeRange, OperativeRange } from 'app/shared/model/operative-range.model';

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
    selectedOpConditions: IOperativeRange[] = [];

    selectedFromDate: string;
    selectedToDate: string;

    opCondDisabled = true;
    sensorsDisabled = true;
    tagsDisabled = true;

    searchParameters: SampleSearchParameters;
    searchReturned = false;
    searchResults: Number = 0;
    batchTaskId = '';

    isSearching: boolean;
    unusedSelectedOpCond: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected targetSystemService: TargetSystemService,
        protected tagService: TagService,
        protected samplingService: SamplingService,
        protected searchSampleService: SearchSampleService
    ) {}

    ngOnInit() {
        this.isSearching = false;
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

        if (selected) {
            this.initOpConditions(selected);
            this.queryTags(selected.id);
            this.querySensors(selected.id);
        } else {
            this.opCondDisabled = true;
            this.sensorsDisabled = true;
            this.tagsDisabled = true;
            this.selectedTags = [];
            this.selectedSensors = [];
            this.selectedOpConditions = [];
        }
    }

    private initOpConditions(selected: ITargetSystem) {
        this.selectedOpConditions = [];
        this.operativeConditions = [];
        this.opCondDisabled = false;
        this.operativeConditions = Array.from(new Set(selected.operativeRanges.map(value => value.varName.toLowerCase())));
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

    search() {
        this.searchParameters.targetSystemId = [this.selectedTargetSystem.id];
        this.searchParameters.tags = this.selectedTags;
        this.searchParameters.sensorsId = this.selectedSensors;
        this.searchParameters.operativeConditions = this.selectedOpConditions;
        this.searchParameters.fromDateTime = this.selectedFromDate != null ? moment(this.selectedFromDate, DATE_TIME_FORMAT) : null;
        this.searchParameters.toDateTime = this.selectedToDate != null ? moment(this.selectedToDate, DATE_TIME_FORMAT) : null;
        // console.log('search with', this.searchParameters);
        this.isSearching = true;
        this.searchSampleService
            .search(this.searchParameters)
            .subscribe(
                (res: HttpResponse<Array<string>>) => this.onSearchSuccess(res),
                (res: HttpErrorResponse) => this.onSearchError(res)
            );
    }

    private onSearchSuccess(res: HttpResponse<Array<string>>) {
        this.searchReturned = true;
        this.isSearching = false;
        this.searchResults = res.body['count'];
        this.batchTaskId = res.body['batchTaskId'];
    }

    private onSearchError(res: HttpErrorResponse) {
        this.searchReturned = false;
        this.isSearching = false;
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

    opConditionsListPlaceHolder() {
        if (this.opCondDisabled) {
            return '';
        } else if (this.operativeConditions.length === 0) {
            return 'Target System doesn\'t have operative conditions';
        } else {
            return 'Select one or more operative conditions';
        }
    }

    addOpCondSelected(opCondAdded) {
        this.selectedOpConditions.push(new OperativeRange(opCondAdded));
    }

    removeOpCondSelected($event) {
        let removeIndex = 0;
        this.selectedOpConditions.forEach((value, index) => {
            if (value.varName === $event.value) {
                removeIndex = index;
            }
        });

        this.selectedOpConditions.splice(removeIndex, 1);
    }

    resetOpCondSelected() {
        this.selectedOpConditions = [];
    }
}
