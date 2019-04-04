/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { KanefwTestModule } from '../../../test.module';
import { HardwareFileUpdateComponent } from 'app/entities/hardware-file/hardware-file-update.component';
import { HardwareFileService } from 'app/entities/hardware-file/hardware-file.service';
import { HardwareFile } from 'app/shared/model/hardware-file.model';

describe('Component Tests', () => {
    describe('HardwareFile Management Update Component', () => {
        let comp: HardwareFileUpdateComponent;
        let fixture: ComponentFixture<HardwareFileUpdateComponent>;
        let service: HardwareFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [HardwareFileUpdateComponent]
            })
                .overrideTemplate(HardwareFileUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HardwareFileUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HardwareFileService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new HardwareFile(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.hardwareFile = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new HardwareFile();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.hardwareFile = entity;
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
