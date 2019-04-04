/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DeviceHealthService } from 'app/entities/device-health/device-health.service';
import { IDeviceHealth, DeviceHealth } from 'app/shared/model/device-health.model';

describe('Service Tests', () => {
    describe('DeviceHealth Service', () => {
        let injector: TestBed;
        let service: DeviceHealthService;
        let httpMock: HttpTestingController;
        let elemDefault: IDeviceHealth;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(DeviceHealthService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new DeviceHealth(
                0,
                currentDate,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                false,
                false,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA'
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        timeStamp: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a DeviceHealth', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        timeStamp: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        timeStamp: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new DeviceHealth(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a DeviceHealth', async () => {
                const returnedFromService = Object.assign(
                    {
                        timeStamp: currentDate.format(DATE_TIME_FORMAT),
                        rssi: 'BBBBBB',
                        locationLat: 'BBBBBB',
                        locationLong: 'BBBBBB',
                        devicePhoneNumber: 'BBBBBB',
                        deviceCarrier: 'BBBBBB',
                        printerStatus: 'BBBBBB',
                        updateAvailable: true,
                        updateRequired: true,
                        newAppVersion: 'BBBBBB',
                        otaServerIp: 'BBBBBB',
                        newAppFileName: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        timeStamp: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of DeviceHealth', async () => {
                const returnedFromService = Object.assign(
                    {
                        timeStamp: currentDate.format(DATE_TIME_FORMAT),
                        rssi: 'BBBBBB',
                        locationLat: 'BBBBBB',
                        locationLong: 'BBBBBB',
                        devicePhoneNumber: 'BBBBBB',
                        deviceCarrier: 'BBBBBB',
                        printerStatus: 'BBBBBB',
                        updateAvailable: true,
                        updateRequired: true,
                        newAppVersion: 'BBBBBB',
                        otaServerIp: 'BBBBBB',
                        newAppFileName: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        timeStamp: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a DeviceHealth', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
