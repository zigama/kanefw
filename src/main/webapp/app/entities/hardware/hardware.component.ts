import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IHardware } from 'app/shared/model/hardware.model';
import { AccountService } from 'app/core';
import { HardwareService } from './hardware.service';

@Component({
    selector: 'jhi-hardware',
    templateUrl: './hardware.component.html'
})
export class HardwareComponent implements OnInit, OnDestroy {
    hardwares: IHardware[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected hardwareService: HardwareService,
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
            this.hardwareService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IHardware[]>) => res.ok),
                    map((res: HttpResponse<IHardware[]>) => res.body)
                )
                .subscribe((res: IHardware[]) => (this.hardwares = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.hardwareService
            .query()
            .pipe(
                filter((res: HttpResponse<IHardware[]>) => res.ok),
                map((res: HttpResponse<IHardware[]>) => res.body)
            )
            .subscribe(
                (res: IHardware[]) => {
                    this.hardwares = res;
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
        this.registerChangeInHardwares();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IHardware) {
        return item.id;
    }

    registerChangeInHardwares() {
        this.eventSubscriber = this.eventManager.subscribe('hardwareListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
