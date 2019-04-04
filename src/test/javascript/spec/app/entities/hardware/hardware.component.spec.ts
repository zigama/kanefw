/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KanefwTestModule } from '../../../test.module';
import { HardwareComponent } from 'app/entities/hardware/hardware.component';
import { HardwareService } from 'app/entities/hardware/hardware.service';
import { Hardware } from 'app/shared/model/hardware.model';

describe('Component Tests', () => {
    describe('Hardware Management Component', () => {
        let comp: HardwareComponent;
        let fixture: ComponentFixture<HardwareComponent>;
        let service: HardwareService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [HardwareComponent],
                providers: []
            })
                .overrideTemplate(HardwareComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HardwareComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HardwareService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Hardware(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.hardwares[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
