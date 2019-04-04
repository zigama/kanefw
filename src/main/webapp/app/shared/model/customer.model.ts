import { ITransaction } from 'app/shared/model/transaction.model';

export const enum IDType {
    NATIONALID = 'NATIONALID',
    PASSPORT = 'PASSPORT'
}

export interface ICustomer {
    id?: number;
    idNumber?: string;
    idType?: IDType;
    firstName?: string;
    middleName?: string;
    lastName?: string;
    currentBalance?: number;
    accountNumber?: string;
    pin?: string;
    transactions?: ITransaction[];
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public idNumber?: string,
        public idType?: IDType,
        public firstName?: string,
        public middleName?: string,
        public lastName?: string,
        public currentBalance?: number,
        public accountNumber?: string,
        public pin?: string,
        public transactions?: ITransaction[]
    ) {}
}
