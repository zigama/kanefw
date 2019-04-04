import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IHardwareFile } from 'app/shared/model/hardware-file.model';
import { HardwareFileService } from './hardware-file.service';
import { IContent } from 'app/shared/model/content.model';
import { ContentService } from 'app/entities/content';
import { IHardware } from 'app/shared/model/hardware.model';
import { HardwareService } from 'app/entities/hardware';

@Component({
    selector: 'jhi-hardware-file-update',
    templateUrl: './hardware-file-update.component.html'
})
export class HardwareFileUpdateComponent implements OnInit {
    hardwareFile: IHardwareFile;
    isSaving: boolean;

    contents: IContent[];

    hardwares: IHardware[];
    dateUploadedDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected hardwareFileService: HardwareFileService,
        protected contentService: ContentService,
        protected hardwareService: HardwareService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ hardwareFile }) => {
            this.hardwareFile = hardwareFile;
        });
        this.contentService
            .query({ filter: 'hardwarefile-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IContent[]>) => mayBeOk.ok),
                map((response: HttpResponse<IContent[]>) => response.body)
            )
            .subscribe(
                (res: IContent[]) => {
                    if (!this.hardwareFile.content || !this.hardwareFile.content.id) {
                        this.contents = res;
                    } else {
                        this.contentService
                            .find(this.hardwareFile.content.id)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IContent>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IContent>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IContent) => (this.contents = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
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
        if (this.hardwareFile.id !== undefined) {
            this.subscribeToSaveResponse(this.hardwareFileService.update(this.hardwareFile));
        } else {
            this.subscribeToSaveResponse(this.hardwareFileService.create(this.hardwareFile));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IHardwareFile>>) {
        result.subscribe((res: HttpResponse<IHardwareFile>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackContentById(index: number, item: IContent) {
        return item.id;
    }

    trackHardwareById(index: number, item: IHardware) {
        return item.id;
    }
}
