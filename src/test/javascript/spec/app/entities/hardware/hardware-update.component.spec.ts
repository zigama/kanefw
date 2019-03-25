/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { KanefwTestModule } from '../../../test.module';
import { HardwareUpdateComponent } from 'app/entities/hardware/hardware-update.component';
import { HardwareService } from 'app/entities/hardware/hardware.service';
import { Hardware } from 'app/shared/model/hardware.model';

describe('Component Tests', () => {
    describe('Hardware Management Update Component', () => {
        let comp: HardwareUpdateComponent;
        let fixture: ComponentFixture<HardwareUpdateComponent>;
        let service: HardwareService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [HardwareUpdateComponent]
            })
                .overrideTemplate(HardwareUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HardwareUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HardwareService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Hardware(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.hardware = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Hardware();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.hardware = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
