/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { HardwareFileService } from 'app/entities/hardware-file/hardware-file.service';
import { IHardwareFile, HardwareFile, FileCategory } from 'app/shared/model/hardware-file.model';

describe('Service Tests', () => {
    describe('HardwareFile Service', () => {
        let injector: TestBed;
        let service: HardwareFileService;
        let httpMock: HttpTestingController;
        let elemDefault: IHardwareFile;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(HardwareFileService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new HardwareFile(0, 'AAAAAAA', 0, 'AAAAAAA', currentDate, 'AAAAAAA', FileCategory.FIRMWARE);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        dateUploaded: currentDate.format(DATE_FORMAT)
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

            it('should create a HardwareFile', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        dateUploaded: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateUploaded: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new HardwareFile(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a HardwareFile', async () => {
                const returnedFromService = Object.assign(
                    {
                        title: 'BBBBBB',
                        size: 1,
                        mimeType: 'BBBBBB',
                        dateUploaded: currentDate.format(DATE_FORMAT),
                        version: 'BBBBBB',
                        category: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        dateUploaded: currentDate
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

            it('should return a list of HardwareFile', async () => {
                const returnedFromService = Object.assign(
                    {
                        title: 'BBBBBB',
                        size: 1,
                        mimeType: 'BBBBBB',
                        dateUploaded: currentDate.format(DATE_FORMAT),
                        version: 'BBBBBB',
                        category: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateUploaded: currentDate
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

            it('should delete a HardwareFile', async () => {
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
