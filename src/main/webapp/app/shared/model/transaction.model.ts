import { Moment } from 'moment';
import { IDevice } from 'app/shared/model/device.model';
import { ICustomer } from 'app/shared/model/customer.model';

export interface ITransaction {
    id?: number;
    timeStamp?: Moment;
    transactionAmount?: number;
    device?: IDevice;
    customer?: ICustomer;
}

export class Transaction implements ITransaction {
    constructor(
        public id?: number,
        public timeStamp?: Moment,
        public transactionAmount?: number,
        public device?: IDevice,
        public customer?: ICustomer
    ) {}
}
