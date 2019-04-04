import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IContent } from 'app/shared/model/content.model';
import { ContentService } from './content.service';
import { IHardwareFile } from 'app/shared/model/hardware-file.model';
import { HardwareFileService } from 'app/entities/hardware-file';

@Component({
    selector: 'jhi-content-update',
    templateUrl: './content-update.component.html'
})
export class ContentUpdateComponent implements OnInit {
    content: IContent;
    isSaving: boolean;

    hardwarefiles: IHardwareFile[];

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected contentService: ContentService,
        protected hardwareFileService: HardwareFileService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ content }) => {
            this.content = content;
        });
        this.hardwareFileService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IHardwareFile[]>) => mayBeOk.ok),
                map((response: HttpResponse<IHardwareFile[]>) => response.body)
            )
            .subscribe((res: IHardwareFile[]) => (this.hardwarefiles = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.content.id !== undefined) {
            this.subscribeToSaveResponse(this.contentService.update(this.content));
        } else {
            this.subscribeToSaveResponse(this.contentService.create(this.content));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IContent>>) {
        result.subscribe((res: HttpResponse<IContent>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackHardwareFileById(index: number, item: IHardwareFile) {
        return item.id;
    }
}
