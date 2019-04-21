import {Component, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {TargetSystemService} from 'app/entities/target-system';
import {ITargetSystem} from 'app/shared/model/target-system.model';
import {filter, map} from 'rxjs/operators';
import {JhiAlertService} from 'ng-jhipster';
import {Observable, of} from 'rxjs';
import {TagService} from 'app/entities/tag';
import {ITag} from 'app/shared/model/tag.model';

@Component({
    selector: 'jhi-search-sample',
    templateUrl: './search-sample.component.html'
})
export class SearchSampleComponent implements OnInit {
    private targetSystems: Observable<ITargetSystem[]>;
    private tags: Observable<ITag[]>;
    private operativeConditions: string[];

    private selectedTargetSystem: ITargetSystem;
    private selectedTags: ITag[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected targetSystemService: TargetSystemService,
        protected tagService: TagService
    ) {
    }

    ngOnInit() {
        this.targetSystemService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ITargetSystem[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITargetSystem[]>) => response.body)
            )
            .subscribe(
                (res: ITargetSystem[]) => (this.targetSystems = of(res)),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.tagService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ITag[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITag[]>) => response.body)
            )
            .subscribe(
                (res: ITag[]) => (this.tags = of(res)),
                (res: HttpErrorResponse) => this.onError(res.message)
            );

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
}
