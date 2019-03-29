import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IHardware } from 'app/shared/model/hardware.model';
import { HardwareService } from './hardware.service';

@Component({
    selector: 'jhi-hardware-update',
    templateUrl: './hardware-update.component.html'
})
export class HardwareUpdateComponent implements OnInit {
    hardware: IHardware;
    isSaving: boolean;

    constructor(protected hardwareService: HardwareService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ hardware }) => {
            this.hardware = hardware;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.hardware.id !== undefined) {
            this.subscribeToSaveResponse(this.hardwareService.update(this.hardware));
        } else {
            this.subscribeToSaveResponse(this.hardwareService.create(this.hardware));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IHardware>>) {
        result.subscribe((res: HttpResponse<IHardware>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
