import { IDeviceHealth } from 'app/shared/model/device-health.model';
import { ITransaction } from 'app/shared/model/transaction.model';
import { IHardware } from 'app/shared/model/hardware.model';

export interface IDevice {
    id?: number;
    serialNumber?: string;
    deviceHealths?: IDeviceHealth[];
    transactions?: ITransaction[];
    hardware?: IHardware;
}

export class Device implements IDevice {
    constructor(
        public id?: number,
        public serialNumber?: string,
        public deviceHealths?: IDeviceHealth[],
        public transactions?: ITransaction[],
        public hardware?: IHardware
    ) {}
}
