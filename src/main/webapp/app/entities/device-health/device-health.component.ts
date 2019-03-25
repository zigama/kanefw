import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDeviceHealth } from 'app/shared/model/device-health.model';
import { AccountService } from 'app/core';
import { DeviceHealthService } from './device-health.service';

@Component({
    selector: 'jhi-device-health',
    templateUrl: './device-health.component.html'
})
export class DeviceHealthComponent implements OnInit, OnDestroy {
    deviceHealths: IDeviceHealth[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected deviceHealthService: DeviceHealthService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.deviceHealthService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IDeviceHealth[]>) => res.ok),
                    map((res: HttpResponse<IDeviceHealth[]>) => res.body)
                )
                .subscribe((res: IDeviceHealth[]) => (this.deviceHealths = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.deviceHealthService
            .query()
            .pipe(
                filter((res: HttpResponse<IDeviceHealth[]>) => res.ok),
                map((res: HttpResponse<IDeviceHealth[]>) => res.body)
            )
            .subscribe(
                (res: IDeviceHealth[]) => {
                    this.deviceHealths = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDeviceHealths();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDeviceHealth) {
        return item.id;
    }

    registerChangeInDeviceHealths() {
        this.eventSubscriber = this.eventManager.subscribe('deviceHealthListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
