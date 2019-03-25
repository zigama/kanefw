import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDevice } from 'app/shared/model/device.model';
import { AccountService } from 'app/core';
import { DeviceService } from './device.service';

@Component({
    selector: 'jhi-device',
    templateUrl: './device.component.html'
})
export class DeviceComponent implements OnInit, OnDestroy {
    devices: IDevice[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected deviceService: DeviceService,
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
            this.deviceService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IDevice[]>) => res.ok),
                    map((res: HttpResponse<IDevice[]>) => res.body)
                )
                .subscribe((res: IDevice[]) => (this.devices = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.deviceService
            .query()
            .pipe(
                filter((res: HttpResponse<IDevice[]>) => res.ok),
                map((res: HttpResponse<IDevice[]>) => res.body)
            )
            .subscribe(
                (res: IDevice[]) => {
                    this.devices = res;
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
        this.registerChangeInDevices();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDevice) {
        return item.id;
    }

    registerChangeInDevices() {
        this.eventSubscriber = this.eventManager.subscribe('deviceListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
