import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDeviceHealth } from 'app/shared/model/device-health.model';
import { DeviceHealthService } from './device-health.service';

@Component({
    selector: 'jhi-device-health-delete-dialog',
    templateUrl: './device-health-delete-dialog.component.html'
})
export class DeviceHealthDeleteDialogComponent {
    deviceHealth: IDeviceHealth;

    constructor(
        protected deviceHealthService: DeviceHealthService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.deviceHealthService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'deviceHealthListModification',
                content: 'Deleted an deviceHealth'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-device-health-delete-popup',
    template: ''
})
export class DeviceHealthDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ deviceHealth }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DeviceHealthDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.deviceHealth = deviceHealth;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/device-health', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/device-health', { outlets: { popup: null } }]);
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
