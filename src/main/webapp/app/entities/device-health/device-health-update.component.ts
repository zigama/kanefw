import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IDeviceHealth } from 'app/shared/model/device-health.model';
import { DeviceHealthService } from './device-health.service';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from 'app/entities/device';

@Component({
    selector: 'jhi-device-health-update',
    templateUrl: './device-health-update.component.html'
})
export class DeviceHealthUpdateComponent implements OnInit {
    deviceHealth: IDeviceHealth;
    isSaving: boolean;

    devices: IDevice[];
    timeStamp: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected deviceHealthService: DeviceHealthService,
        protected deviceService: DeviceService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ deviceHealth }) => {
            this.deviceHealth = deviceHealth;
            this.timeStamp = this.deviceHealth.timeStamp != null ? this.deviceHealth.timeStamp.format(DATE_TIME_FORMAT) : null;
        });
        this.deviceService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDevice[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDevice[]>) => response.body)
            )
            .subscribe((res: IDevice[]) => (this.devices = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.deviceHealth.timeStamp = this.timeStamp != null ? moment(this.timeStamp, DATE_TIME_FORMAT) : null;
        if (this.deviceHealth.id !== undefined) {
            this.subscribeToSaveResponse(this.deviceHealthService.update(this.deviceHealth));
        } else {
            this.subscribeToSaveResponse(this.deviceHealthService.create(this.deviceHealth));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceHealth>>) {
        result.subscribe((res: HttpResponse<IDeviceHealth>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackDeviceById(index: number, item: IDevice) {
        return item.id;
    }
}
