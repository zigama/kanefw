import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IHardwareFile } from 'app/shared/model/hardware-file.model';
import { AccountService } from 'app/core';
import { HardwareFileService } from './hardware-file.service';

@Component({
    selector: 'jhi-hardware-file',
    templateUrl: './hardware-file.component.html'
})
export class HardwareFileComponent implements OnInit, OnDestroy {
    hardwareFiles: IHardwareFile[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected hardwareFileService: HardwareFileService,
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
            this.hardwareFileService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IHardwareFile[]>) => res.ok),
                    map((res: HttpResponse<IHardwareFile[]>) => res.body)
                )
                .subscribe((res: IHardwareFile[]) => (this.hardwareFiles = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.hardwareFileService
            .query()
            .pipe(
                filter((res: HttpResponse<IHardwareFile[]>) => res.ok),
                map((res: HttpResponse<IHardwareFile[]>) => res.body)
            )
            .subscribe(
                (res: IHardwareFile[]) => {
                    this.hardwareFiles = res;
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
        this.registerChangeInHardwareFiles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IHardwareFile) {
        return item.id;
    }

    registerChangeInHardwareFiles() {
        this.eventSubscriber = this.eventManager.subscribe('hardwareFileListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
