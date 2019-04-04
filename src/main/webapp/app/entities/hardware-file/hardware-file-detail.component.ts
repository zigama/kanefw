import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHardwareFile } from 'app/shared/model/hardware-file.model';

@Component({
    selector: 'jhi-hardware-file-detail',
    templateUrl: './hardware-file-detail.component.html'
})
export class HardwareFileDetailComponent implements OnInit {
    hardwareFile: IHardwareFile;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ hardwareFile }) => {
            this.hardwareFile = hardwareFile;
        });
    }

    previousState() {
        window.history.back();
    }
}
