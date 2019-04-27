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
    private targetSystems: Observable<ITargetSystem[]>;
    private tags: Observable<ITag[]>;
    private operativeConditions: string[];
    private sensors: string[];

    private selectedTargetSystem: ITargetSystem;
    private selectedTags: string[];
    private selectedSensors: string[];

    private selectedFromDate: string;
    private selectedToDate: string;

    private searchParameters: SampleSearchParameters;
    private searchReturned = false;
    private searchResults: Number = 0;

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
        this.tagService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ITag[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITag[]>) => response.body)
            )
            .subscribe((res: ITag[]) => (this.tags = of(res)), (res: HttpErrorResponse) => this.onError(res.message));
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onChangeTargetSystem(selected: ITargetSystem) {
        console.log('selected system ', selected);
        this.operativeConditions = Array.from(new Set(selected.operativeRanges.map(value => value.varName)));
        console.log('selected tags ', this.selectedTags);
        console.log('filtered conditions ', this.operativeConditions);
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
            .subscribe((res: HttpResponse<Number>) => this.onSearchSuccess(res), (res: HttpErrorResponse) => this.onSearchError(res));
    }

    private onSearchSuccess(res: HttpResponse<Number>) {
        this.searchReturned = true;
        this.searchResults = res.body;
        console.log('search success ', res.body);
    }

    private onSearchError(res: HttpErrorResponse) {
        this.searchReturned = false;
        console.log('search error ', res);
    }
}
