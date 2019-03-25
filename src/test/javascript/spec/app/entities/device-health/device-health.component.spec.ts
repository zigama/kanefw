/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KanefwTestModule } from '../../../test.module';
import { DeviceHealthComponent } from 'app/entities/device-health/device-health.component';
import { DeviceHealthService } from 'app/entities/device-health/device-health.service';
import { DeviceHealth } from 'app/shared/model/device-health.model';

describe('Component Tests', () => {
    describe('DeviceHealth Management Component', () => {
        let comp: DeviceHealthComponent;
        let fixture: ComponentFixture<DeviceHealthComponent>;
        let service: DeviceHealthService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [DeviceHealthComponent],
                providers: []
            })
                .overrideTemplate(DeviceHealthComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DeviceHealthComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeviceHealthService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new DeviceHealth(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.deviceHealths[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
