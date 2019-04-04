import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHardwareFile } from 'app/shared/model/hardware-file.model';

type EntityResponseType = HttpResponse<IHardwareFile>;
type EntityArrayResponseType = HttpResponse<IHardwareFile[]>;

@Injectable({ providedIn: 'root' })
export class HardwareFileService {
    public resourceUrl = SERVER_API_URL + 'api/hardware-files';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/hardware-files';

    constructor(protected http: HttpClient) {}

    create(hardwareFile: IHardwareFile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(hardwareFile);
        return this.http
            .post<IHardwareFile>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(hardwareFile: IHardwareFile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(hardwareFile);
        return this.http
            .put<IHardwareFile>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IHardwareFile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IHardwareFile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IHardwareFile[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(hardwareFile: IHardwareFile): IHardwareFile {
        const copy: IHardwareFile = Object.assign({}, hardwareFile, {
            dateUploaded:
                hardwareFile.dateUploaded != null && hardwareFile.dateUploaded.isValid()
                    ? hardwareFile.dateUploaded.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateUploaded = res.body.dateUploaded != null ? moment(res.body.dateUploaded) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((hardwareFile: IHardwareFile) => {
                hardwareFile.dateUploaded = hardwareFile.dateUploaded != null ? moment(hardwareFile.dateUploaded) : null;
            });
        }
        return res;
    }
}
