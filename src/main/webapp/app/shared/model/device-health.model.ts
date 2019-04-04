import { Moment } from 'moment';
import { IDevice } from 'app/shared/model/device.model';

export interface IDeviceHealth {
    id?: number;
    timeStamp?: Moment;
    rssi?: string;
    locationLat?: string;
    locationLong?: string;
    devicePhoneNumber?: string;
    deviceCarrier?: string;
    printerStatus?: string;
    updateAvailable?: boolean;
    updateRequired?: boolean;
    newAppVersion?: string;
    otaServerIp?: string;
    newAppFileName?: string;
    device?: IDevice;
}

export class DeviceHealth implements IDeviceHealth {
    constructor(
        public id?: number,
        public timeStamp?: Moment,
        public rssi?: string,
        public locationLat?: string,
        public locationLong?: string,
        public devicePhoneNumber?: string,
        public deviceCarrier?: string,
        public printerStatus?: string,
        public updateAvailable?: boolean,
        public updateRequired?: boolean,
        public newAppVersion?: string,
        public otaServerIp?: string,
        public newAppFileName?: string,
        public device?: IDevice
    ) {
        this.updateAvailable = this.updateAvailable || false;
        this.updateRequired = this.updateRequired || false;
    }
}
