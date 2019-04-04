import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeviceHealth } from 'app/shared/model/device-health.model';

@Component({
    selector: 'jhi-device-health-detail',
    templateUrl: './device-health-detail.component.html'
})
export class DeviceHealthDetailComponent implements OnInit {
    deviceHealth: IDeviceHealth;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ deviceHealth }) => {
            this.deviceHealth = deviceHealth;
        });
    }

    previousState() {
        window.history.back();
    }
}
