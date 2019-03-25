import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHardware } from 'app/shared/model/hardware.model';
import { HardwareService } from './hardware.service';

@Component({
    selector: 'jhi-hardware-delete-dialog',
    templateUrl: './hardware-delete-dialog.component.html'
})
export class HardwareDeleteDialogComponent {
    hardware: IHardware;

    constructor(protected hardwareService: HardwareService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.hardwareService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'hardwareListModification',
                content: 'Deleted an hardware'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-hardware-delete-popup',
    template: ''
})
export class HardwareDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ hardware }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(HardwareDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.hardware = hardware;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/hardware', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/hardware', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
