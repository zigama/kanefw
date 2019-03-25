import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHardware } from 'app/shared/model/hardware.model';

@Component({
    selector: 'jhi-hardware-detail',
    templateUrl: './hardware-detail.component.html'
})
export class HardwareDetailComponent implements OnInit {
    hardware: IHardware;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ hardware }) => {
            this.hardware = hardware;
        });
    }

    previousState() {
        window.history.back();
    }
}
