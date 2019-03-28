import { IHardwareFile } from 'app/shared/model/hardware-file.model';
import { IDevice } from 'app/shared/model/device.model';

export interface IHardware {
    id?: number;
    model?: string;
    serie?: string;
    hardwareFiles?: IHardwareFile[];
    devices?: IDevice[];
}

export class Hardware implements IHardware {
    constructor(
        public id?: number,
        public model?: string,
        public serie?: string,
        public hardwareFiles?: IHardwareFile[],
        public devices?: IDevice[]
    ) {}
}
