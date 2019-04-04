import { IHardwareFile } from 'app/shared/model/hardware-file.model';

export interface IContent {
    id?: number;
    dataContentType?: string;
    data?: any;
    hardwareFile?: IHardwareFile;
}

export class Content implements IContent {
    constructor(public id?: number, public dataContentType?: string, public data?: any, public hardwareFile?: IHardwareFile) {}
}
