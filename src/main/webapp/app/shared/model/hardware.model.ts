import { IHardwareFile } from 'app/shared/model/hardware-file.model';

export interface IHardware {
    id?: number;
    model?: string;
    serie?: string;
    hardwareFiles?: IHardwareFile[];
}

export class Hardware implements IHardware {
    constructor(public id?: number, public model?: string, public serie?: string, public hardwareFiles?: IHardwareFile[]) {}
}
