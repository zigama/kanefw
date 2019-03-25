import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ITransaction } from 'app/shared/model/transaction.model';
import { AccountService } from 'app/core';
import { TransactionService } from './transaction.service';

@Component({
    selector: 'jhi-transaction',
    templateUrl: './transaction.component.html'
})
export class TransactionComponent implements OnInit, OnDestroy {
    transactions: ITransaction[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected transactionService: TransactionService,
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
            this.transactionService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<ITransaction[]>) => res.ok),
                    map((res: HttpResponse<ITransaction[]>) => res.body)
                )
                .subscribe((res: ITransaction[]) => (this.transactions = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.transactionService
            .query()
            .pipe(
                filter((res: HttpResponse<ITransaction[]>) => res.ok),
                map((res: HttpResponse<ITransaction[]>) => res.body)
            )
            .subscribe(
                (res: ITransaction[]) => {
                    this.transactions = res;
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
        this.registerChangeInTransactions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ITransaction) {
        return item.id;
    }

    registerChangeInTransactions() {
        this.eventSubscriber = this.eventManager.subscribe('transactionListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
