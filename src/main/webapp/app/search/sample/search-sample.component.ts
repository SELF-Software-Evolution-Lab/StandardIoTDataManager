import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { TargetSystemService } from 'app/entities/target-system';
import { ITargetSystem } from 'app/shared/model/target-system.model';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { Observable, of } from 'rxjs';
import { TagService } from 'app/entities/tag';
import { ITag } from 'app/shared/model/tag.model';
import { SearchSampleService } from 'app/search/sample/sample-search.service';
import { SampleSearchParameters } from 'app/shared/model/sample-search-parameters.model';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

@Component({
    selector: 'jhi-search-sample',
    templateUrl: './search-sample.component.html'
})
export class SearchSampleComponent implements OnInit {
    targetSystems: Observable<ITargetSystem[]>;
    tags: ITag[];
    operativeConditions: string[];
    sensors: string[];

    selectedTargetSystem: ITargetSystem;
    selectedTags: string[];
    selectedSensors: string[];

    selectedFromDate: string;
    selectedToDate: string;

    searchParameters: SampleSearchParameters;
    searchReturned = false;
    searchResults: Number = 0;
    batchTaskId = '';
    tagListDisabled = true;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected targetSystemService: TargetSystemService,
        protected tagService: TagService,
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
    }

    private queryTags(tsId: string) {
        this.tagService
            .listByTargetSystem(tsId)
            .pipe(
                filter((mayBeOk: HttpResponse<ITag[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITag[]>) => response.body)
            )
            .subscribe(
                (res: ITag[]) => {
                    this.tags = res;
                    this.tagListDisabled = false;
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
        this.searchParameters.fromDateTime = this.selectedFromDate != null ? moment(this.selectedFromDate, DATE_TIME_FORMAT) : null;
        this.searchParameters.toDateTime = this.selectedToDate != null ? moment(this.selectedToDate, DATE_TIME_FORMAT) : null;
        console.log('search', this.searchParameters);
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

    tagListCondition(): boolean {
        return this.tagListDisabled;
    }

    tagListPlaceHolder() {
        if (this.tagListDisabled) {
            return '';
        } else if (this.tags.length === 0) {
            return 'No tags found for selected Target System';
        } else {
            return 'Select one or more tags';
        }
    }
}
