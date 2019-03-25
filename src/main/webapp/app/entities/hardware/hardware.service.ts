import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHardware } from 'app/shared/model/hardware.model';

type EntityResponseType = HttpResponse<IHardware>;
type EntityArrayResponseType = HttpResponse<IHardware[]>;

@Injectable({ providedIn: 'root' })
export class HardwareService {
    public resourceUrl = SERVER_API_URL + 'api/hardwares';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/hardwares';

    constructor(protected http: HttpClient) {}

    create(hardware: IHardware): Observable<EntityResponseType> {
        return this.http.post<IHardware>(this.resourceUrl, hardware, { observe: 'response' });
    }

    update(hardware: IHardware): Observable<EntityResponseType> {
        return this.http.put<IHardware>(this.resourceUrl, hardware, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IHardware>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHardware[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHardware[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
