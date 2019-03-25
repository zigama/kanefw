import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHardwareFile } from 'app/shared/model/hardware-file.model';
import { HardwareFileService } from './hardware-file.service';

@Component({
    selector: 'jhi-hardware-file-delete-dialog',
    templateUrl: './hardware-file-delete-dialog.component.html'
})
export class HardwareFileDeleteDialogComponent {
    hardwareFile: IHardwareFile;

    constructor(
        protected hardwareFileService: HardwareFileService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.hardwareFileService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'hardwareFileListModification',
                content: 'Deleted an hardwareFile'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-hardware-file-delete-popup',
    template: ''
})
export class HardwareFileDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ hardwareFile }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(HardwareFileDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.hardwareFile = hardwareFile;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/hardware-file', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/hardware-file', { outlets: { popup: null } }]);
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
