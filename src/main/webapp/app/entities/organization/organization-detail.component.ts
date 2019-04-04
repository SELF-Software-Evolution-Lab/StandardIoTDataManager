import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

import { IOrganization } from 'app/shared/model/organization.model';
import { UserService, User } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-organization-detail',
    templateUrl: './organization-detail.component.html'
})
export class OrganizationDetailComponent implements OnInit {
    organization: IOrganization;
    users: User[];

    constructor(protected activatedRoute: ActivatedRoute, private userService: UserService, private alertService: JhiAlertService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ organization }) => {
            this.organization = organization;
        });
    }

    previousState() {
        window.history.back();
    }

    private onSuccessUsers(data, headers) {
        this.users = data;
    }

    private onErrorUsers(error) {
        console.log('Error usuarios');
        this.alertService.error(error.error, error.message, null);
    }
}
