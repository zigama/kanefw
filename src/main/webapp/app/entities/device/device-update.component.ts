import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from './device.service';
import { IHardware } from 'app/shared/model/hardware.model';
import { HardwareService } from 'app/entities/hardware';

@Component({
    selector: 'jhi-device-update',
    templateUrl: './device-update.component.html'
})
export class DeviceUpdateComponent implements OnInit {
    device: IDevice;
    isSaving: boolean;

    hardwares: IHardware[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected deviceService: DeviceService,
        protected hardwareService: HardwareService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ device }) => {
            this.device = device;
        });
        this.hardwareService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IHardware[]>) => mayBeOk.ok),
                map((response: HttpResponse<IHardware[]>) => response.body)
            )
            .subscribe((res: IHardware[]) => (this.hardwares = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.device.id !== undefined) {
            this.subscribeToSaveResponse(this.deviceService.update(this.device));
        } else {
            this.subscribeToSaveResponse(this.deviceService.create(this.device));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>) {
        result.subscribe((res: HttpResponse<IDevice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackHardwareById(index: number, item: IHardware) {
        return item.id;
    }
}
