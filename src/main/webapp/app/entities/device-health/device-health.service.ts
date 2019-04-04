import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDeviceHealth } from 'app/shared/model/device-health.model';

type EntityResponseType = HttpResponse<IDeviceHealth>;
type EntityArrayResponseType = HttpResponse<IDeviceHealth[]>;

@Injectable({ providedIn: 'root' })
export class DeviceHealthService {
    public resourceUrl = SERVER_API_URL + 'api/device-healths';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/device-healths';

    constructor(protected http: HttpClient) {}

    create(deviceHealth: IDeviceHealth): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(deviceHealth);
        return this.http
            .post<IDeviceHealth>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(deviceHealth: IDeviceHealth): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(deviceHealth);
        return this.http
            .put<IDeviceHealth>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDeviceHealth>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDeviceHealth[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDeviceHealth[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(deviceHealth: IDeviceHealth): IDeviceHealth {
        const copy: IDeviceHealth = Object.assign({}, deviceHealth, {
            timeStamp: deviceHealth.timeStamp != null && deviceHealth.timeStamp.isValid() ? deviceHealth.timeStamp.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.timeStamp = res.body.timeStamp != null ? moment(res.body.timeStamp) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((deviceHealth: IDeviceHealth) => {
                deviceHealth.timeStamp = deviceHealth.timeStamp != null ? moment(deviceHealth.timeStamp) : null;
            });
        }
        return res;
    }
}
