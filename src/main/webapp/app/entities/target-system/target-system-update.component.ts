import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITargetSystem } from 'app/shared/model/target-system.model';
import { TargetSystemService } from './target-system.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization';

@Component({
    selector: 'jhi-target-system-update',
    templateUrl: './target-system-update.component.html'
})
export class TargetSystemUpdateComponent implements OnInit {
    targetSystem: ITargetSystem;
    isSaving: boolean;

    organizations: IOrganization[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected targetSystemService: TargetSystemService,
        protected organizationService: OrganizationService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ targetSystem }) => {
            this.targetSystem = targetSystem;
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
}
