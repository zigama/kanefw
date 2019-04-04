/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { KanefwTestModule } from '../../../test.module';
import { DeviceHealthUpdateComponent } from 'app/entities/device-health/device-health-update.component';
import { DeviceHealthService } from 'app/entities/device-health/device-health.service';
import { DeviceHealth } from 'app/shared/model/device-health.model';

describe('Component Tests', () => {
    describe('DeviceHealth Management Update Component', () => {
        let comp: DeviceHealthUpdateComponent;
        let fixture: ComponentFixture<DeviceHealthUpdateComponent>;
        let service: DeviceHealthService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [DeviceHealthUpdateComponent]
            })
                .overrideTemplate(DeviceHealthUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DeviceHealthUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeviceHealthService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new DeviceHealth(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.deviceHealth = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new DeviceHealth();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.deviceHealth = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
