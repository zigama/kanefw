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
    public resourceUrlHF = SERVER_API_URL + 'api/hardwares/files';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/hardwares';

    constructor(protected http: HttpClient) {}

    create(hardware: IHardware): Observable<EntityResponseType> {
        return this.http.post<IHardware>(this.resourceUrl, hardware, { observe: 'response' });
    }

    createHF(hardware: IHardware, files: FileList, version: string): Observable<EntityResponseType> {
        const hardwareMultipartFormParam = 'hardware';
        const filesMultipartFormParam = 'files';
        const versionFormParam = 'version';
        const formData: FormData = new FormData();
        const hardwareAsJsonBlob: Blob = new Blob([JSON.stringify(hardware)], { type: 'application/json' });

        formData.append(hardwareMultipartFormParam, hardwareAsJsonBlob);
        formData.append(versionFormParam, version);
        for (let i = 0; i < files.length; i++) {
            formData.append(filesMultipartFormParam, files.item(i));
        }

        return this.http.post<IHardware>(this.resourceUrlHF, formData, { observe: 'response' });
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
