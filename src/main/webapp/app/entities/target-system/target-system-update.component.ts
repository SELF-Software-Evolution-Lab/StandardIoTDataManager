import { Component, EventEmitter, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITargetSystem } from 'app/shared/model/target-system.model';
import { TargetSystemService } from './target-system.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization';
import { OperativeRangeModalService } from './operative-range-modal.service';
import { IOperativeRange } from 'app/shared/model/operative-range.model';

@Component({
    selector: 'jhi-target-system-update',
    templateUrl: './target-system-update.component.html'
})
export class TargetSystemUpdateComponent implements OnInit {
    targetSystem: ITargetSystem;
    isSaving: boolean;

    organizations: IOrganization[];
    created: string;

    modalRef: EventEmitter<IOperativeRange>;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected targetSystemService: TargetSystemService,
        protected organizationService: OrganizationService,
        protected operativeRangeModalService: OperativeRangeModalService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ targetSystem }) => {
            this.targetSystem = targetSystem;
            this.created = this.targetSystem.created != null ? this.targetSystem.created.format(DATE_TIME_FORMAT) : null;
        });
        this.organizationService
            .query({ filter: 'targetsystem-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IOrganization[]>) => mayBeOk.ok),
                map((response: HttpResponse<IOrganization[]>) => response.body)
            )
            .subscribe(
                (res: IOrganization[]) => {
                    if (!this.targetSystem.organizationId) {
                        this.organizations = res;
                    } else {
                        this.organizationService
                            .find(this.targetSystem.organizationId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IOrganization>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IOrganization>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IOrganization) => (this.organizations = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.targetSystem.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        if (this.targetSystem.id !== undefined) {
            this.subscribeToSaveResponse(this.targetSystemService.update(this.targetSystem));
        } else {
            this.subscribeToSaveResponse(this.targetSystemService.create(this.targetSystem));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ITargetSystem>>) {
        result.subscribe((res: HttpResponse<ITargetSystem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackOrganizationById(index: number, item: IOrganization) {
        return item.id;
    }

    addRange() {
        this.modalRef = this.operativeRangeModalService.openToCreate();
        this.modalRef.subscribe(range => {
            if (range) {
                this.targetSystem.operativeRanges.push(range);
            }
        });
    }

    editRange(i: number) {
        this.modalRef = this.operativeRangeModalService.openToEdit(this.targetSystem.operativeRanges[i]);
        this.modalRef.subscribe(range => {
            console.log('rango retornado edit', range);
            if (range) {
                const prevRange = this.targetSystem.operativeRanges[i];
                if (JSON.stringify(prevRange) === JSON.stringify(range)) {
                    this.targetSystem.operativeRanges.splice(i, 1, range);
                } else {
                    this.jhiAlertService.error("Requested Operative Range doesn't no longer exists", null, null);
                }
            }
        });
    }

    removeRange(i: number) {
        this.targetSystem.operativeRanges.splice(i, 1);
    }
}
